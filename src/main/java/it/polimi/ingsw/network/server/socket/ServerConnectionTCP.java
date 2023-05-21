package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectionTCP implements ServerConnection, Runnable {
    private final Socket socket;
    private InputViewInterface receiver;
    private final ObjectOutputStream out;

    public ServerConnectionTCP(Socket socket) {
        this.socket = socket;

        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            System.err.println("ServerConnectionTCP, line 22: failed to get output stream");
            throw new RuntimeException(e);
        }

        this.out = out;
    }

    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException e) {
            System.err.println("ServerConnectionTCP, line 38: IOException while reading from socket");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.err.println("ServerConnectionTCP, line 41: ClassNotFoundException while reading from socket");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setInputViewInterface(InputViewInterface receiver) {
        this.receiver = receiver;
    }

    private void receive(Object input) {
        if (input instanceof Nickname) {
            receiver.login((Nickname) input);
        } else if (input instanceof GameInitialization) {
            receiver.createGame((GameInitialization) input);
        } else if (input instanceof GameSelection) {
            receiver.selectGame((GameSelection) input);
        } else if (input instanceof LivingRoomSelection) {
            receiver.selectFromLivingRoom((LivingRoomSelection) input);
        } else if (input instanceof BookshelfInsertion) {
            receiver.insertInBookshelf((BookshelfInsertion) input);
        } else if (input instanceof ChatMessage) {
            receiver.writeChat((ChatMessage) input);
        }
    }

    private void sendAsync(Message message) {
        (new Thread(() -> send(message))).start();
    }

    private void send(Message message) {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("ServerConnectionTCP, line 76: failed to send to client");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        sendAsync(update);
    }

    @Override
    public void sendGamesList(GamesList gamesList) {
        sendAsync(gamesList);
    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {
        sendAsync(itemsSelected);
    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {
        sendAsync(acceptedAction);
    }

    @Override
    public void sendLivingRoomAndBookshelvesDimensions(LivingRoomAndBookshelvesDimensions livingRoomAndBookshelvesDimensions) {
        sendAsync(livingRoomAndBookshelvesDimensions);
    }
}
