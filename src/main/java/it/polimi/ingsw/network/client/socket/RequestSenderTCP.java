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

public class RequestSenderTCP implements Runnable, RequestSender {
    private Socket socket;
    private final UpdateReceiver receiver;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public RequestSenderTCP(Socket socket, UpdateReceiver receiver) {
        this.socket = socket;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            Object input;

            while (true) {
                input = in.readObject();

                if (input instanceof GamesList) {
                    receiver.onGamesList((GamesList) input);
                } else if (input instanceof ItemsSelected) {
                    receiver.onItemsSelected((ItemsSelected) input);
                } else if (input instanceof LivingRoomUpdate) {
                    receiver.onLivingRoomUpdate((LivingRoomUpdate) input);
                } else if (input instanceof BookshelfUpdate) {
                    receiver.onBookshelfUUpdate((BookshelfUpdate) input);
                } else if (input instanceof EndingTokenUpdate) {
                    receiver.onEndingTokenUpdate((EndingTokenUpdate) input);
                } else if (input instanceof WaitingUpdate) {
                    receiver.onWaitingUpdate((WaitingUpdate) input);
                } else if (input instanceof ScoresUpdate) {
                    receiver.onScoresUpdate((ScoresUpdate) input);
                } else if (input instanceof CommonGoalCardUpdate) {
                    receiver.onCommonGoalCardUpdate((CommonGoalCardUpdate) input);
                } else if (input instanceof PersonalGoalCardUpdate) {
                    receiver.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) input);
                } else if (input instanceof ChatUpdate) {
                    receiver.onChatUpdate((ChatUpdate) input);
                } else if (input instanceof StartTurnUpdate) {
                    receiver.onStartTurnUpdate((StartTurnUpdate) input);
                } else if (input instanceof EndGameUpdate) {
                    receiver.onEndGameUpdate((EndGameUpdate) input);
                }
            }
        } catch (IOException e) {
            System.err.println("RequestSenderTCP: line 28 = I/O failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("RequestSenderTCP: line 31 = unknown class");
        }
    }

    public synchronized void send(NetworkMessageWithSender message) {
        try {
            out.writeObject(message);
        } catch (IOException e) {
            System.err.println("RequestSenderTCP: line 41 = I/O failed");
            e.printStackTrace();
        }
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
}
