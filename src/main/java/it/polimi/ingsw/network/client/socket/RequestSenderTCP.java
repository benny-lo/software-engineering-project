package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.network.client.ClientReceiver;
import it.polimi.ingsw.network.client.ClientSender;
import it.polimi.ingsw.network.server.ServerSender;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RequestSenderTCP implements ServerSender, ClientSender, Runnable {
    private final Socket socket;
    private final ClientReceiver clientReceiver;
    private ObjectInputStream in;
    private final ObjectOutputStream out;

    public RequestSenderTCP(Socket socket, ClientReceiver clientReceiver) throws IOException {
        this.socket = socket;
        this.clientReceiver = clientReceiver;
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
            clientReceiver.onGamesList((GamesList) object);
        } else if (object instanceof ItemsSelected) {
            clientReceiver.onItemsSelected((ItemsSelected) object);
        } else if (object instanceof LivingRoomUpdate) {
            clientReceiver.onLivingRoomUpdate((LivingRoomUpdate) object);
        } else if (object instanceof BookshelfUpdate) {
            clientReceiver.onBookshelfUpdate((BookshelfUpdate) object);
        } else if (object instanceof EndingTokenUpdate) {
            clientReceiver.onEndingTokenUpdate((EndingTokenUpdate) object);
        } else if (object instanceof WaitingUpdate) {
            clientReceiver.onWaitingUpdate((WaitingUpdate) object);
        } else if (object instanceof ScoresUpdate) {
            clientReceiver.onScoresUpdate((ScoresUpdate) object);
        } else if (object instanceof CommonGoalCardsUpdate) {
            clientReceiver.onCommonGoalCardUpdate((CommonGoalCardsUpdate) object);
        } else if (object instanceof PersonalGoalCardUpdate) {
            clientReceiver.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) object);
        } else if (object instanceof ChatUpdate) {
            clientReceiver.onChatUpdate((ChatUpdate) object);
        } else if (object instanceof StartTurnUpdate) {
            clientReceiver.onStartTurnUpdate((StartTurnUpdate) object);
        } else if (object instanceof EndGameUpdate) {
            clientReceiver.onEndGameUpdate((EndGameUpdate) object);
        } else if (object instanceof AcceptedAction) {
            clientReceiver.onAcceptedAction((AcceptedAction) object);
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

    private void send(NetworkMessage message) {
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

    private void sendAsync(NetworkMessage message) {
        (new Thread(() -> send(message))).start();
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {

    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {

    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {

    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {

    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) {

    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {

    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {

    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {

    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {

    }

    @Override
    public void sendListOfGames(GamesList gamesList) {

    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {

    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {

    }
}
