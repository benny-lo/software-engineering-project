package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.UpdateViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnectionTCP implements ClientConnection, Runnable {
    private final Socket socket;
    private final UpdateViewInterface receiver;
    private ObjectInputStream in;
    private final ObjectOutputStream out;

    public ClientConnectionTCP(Socket socket, UpdateViewInterface receiver) throws IOException {
        this.socket = socket;
        this.receiver = receiver;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void login(Nickname message) {
        sendAsync(message);
    }

    @Override
    public void createGame(GameInitialization message) {
        sendAsync(message);
    }

    @Override
    public void selectGame(GameSelection message) {
        sendAsync(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        sendAsync(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        sendAsync(message);
    }

    @Override
    public void writeChat(ChatMessage message) {
        sendAsync(message);
    }

    private void receive(Object object) {
        if (object instanceof GamesList) {
            receiver.onGamesList((GamesList) object);
        } else if (object instanceof ItemsSelected) {
            receiver.onItemsSelected((ItemsSelected) object);
        } else if (object instanceof LivingRoomUpdate) {
            receiver.onLivingRoomUpdate((LivingRoomUpdate) object);
        } else if (object instanceof BookshelfUpdate) {
            receiver.onBookshelfUpdate((BookshelfUpdate) object);
        } else if (object instanceof EndingTokenUpdate) {
            receiver.onEndingTokenUpdate((EndingTokenUpdate) object);
        } else if (object instanceof WaitingUpdate) {
            receiver.onWaitingUpdate((WaitingUpdate) object);
        } else if (object instanceof ScoresUpdate) {
            receiver.onScoresUpdate((ScoresUpdate) object);
        } else if (object instanceof CommonGoalCardsUpdate) {
            receiver.onCommonGoalCardsUpdate((CommonGoalCardsUpdate) object);
        } else if (object instanceof PersonalGoalCardUpdate) {
            receiver.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) object);
        } else if (object instanceof ChatUpdate) {
            receiver.onChatUpdate((ChatUpdate) object);
        } else if (object instanceof StartTurnUpdate) {
            receiver.onStartTurnUpdate((StartTurnUpdate) object);
        } else if (object instanceof EndGameUpdate) {
            receiver.onEndGameUpdate((EndGameUpdate) object);
        } else if (object instanceof AcceptedAction) {
            receiver.onAcceptedAction((AcceptedAction) object);
        }
    }



    @Override
    public void run() {
        try {
            Object input;
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException e) {
            System.err.println("ClientConnectionTCP: line 28 = I/O failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("ClientConnectionTCP: line 31 = unknown class");
        }
    }

    private void send(Message message) {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("ClientConnectionTCP: line 41 = I/O failed");
                e.printStackTrace();
            }
        }
    }

    private void sendAsync(Message message) {
        (new Thread(() -> send(message))).start();
    }
}
