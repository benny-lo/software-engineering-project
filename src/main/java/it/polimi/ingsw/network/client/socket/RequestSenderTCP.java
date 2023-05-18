package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.client.UpdateReceiver;
import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class RequestSenderTCP implements RequestSender, Runnable {
    private final Socket socket;
    private final UpdateReceiver updateReceiver;
    private ObjectInputStream in;
    private final ObjectOutputStream out;

    public RequestSenderTCP(Socket socket, UpdateReceiver updateReceiver) throws IOException {
        this.socket = socket;
        this.updateReceiver = updateReceiver;
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void login(String nickname) {
        send(new Nickname(nickname));
    }

    @Override
    public void selectGame(String nickname, int id) {
        send(new GameSelection(nickname, id));
    }

    @Override
    public void createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        send(new GameInitialization(nickname, numberPlayers, numberCommonGoals));
    }

    @Override
    public void selectFromLivingRoom(String nickname, List<Position> position) {
        send(new LivingRoomSelection(nickname, position));
    }

    @Override
    public void putInBookshelf(String nickname, int column, List<Integer> permutation) {
        send(new BookshelfInsertion(nickname, column, permutation));
    }

    @Override
    public void addMessage(String nickname, String text) {
        send(new ChatMessage(nickname, text));
    }

    private void receive(Object object) {
        if (object instanceof GamesList) {
            updateReceiver.onGamesList((GamesList) object);
        } else if (object instanceof ItemsSelected) {
            updateReceiver.onItemsSelected((ItemsSelected) object);
        } else if (object instanceof LivingRoomUpdate) {
            updateReceiver.onLivingRoomUpdate((LivingRoomUpdate) object);
        } else if (object instanceof BookshelfUpdate) {
            updateReceiver.onBookshelfUpdate((BookshelfUpdate) object);
        } else if (object instanceof EndingTokenUpdate) {
            updateReceiver.onEndingTokenUpdate((EndingTokenUpdate) object);
        } else if (object instanceof WaitingUpdate) {
            updateReceiver.onWaitingUpdate((WaitingUpdate) object);
        } else if (object instanceof ScoresUpdate) {
            updateReceiver.onScoresUpdate((ScoresUpdate) object);
        } else if (object instanceof CommonGoalCardsUpdate) {
            updateReceiver.onCommonGoalCardUpdate((CommonGoalCardsUpdate) object);
        } else if (object instanceof PersonalGoalCardUpdate) {
            updateReceiver.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) object);
        } else if (object instanceof ChatUpdate) {
            updateReceiver.onChatUpdate((ChatUpdate) object);
        } else if (object instanceof StartTurnUpdate) {
            updateReceiver.onStartTurnUpdate((StartTurnUpdate) object);
        } else if (object instanceof EndGameUpdate) {
            updateReceiver.onEndGameUpdate((EndGameUpdate) object);
        } else if (object instanceof AcceptedAction) {
            updateReceiver.onAcceptedAction((AcceptedAction) object);
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
            System.err.println("RequestSenderTCP: line 28 = I/O failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("RequestSenderTCP: line 31 = unknown class");
        }
    }

    private void send(NetworkMessageWithSender message) {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("RequestSenderTCP: line 41 = I/O failed");
                e.printStackTrace();
            }
        }
    }
}
