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

    private void sendPrivate(Message message) {
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
    public void send(LivingRoomUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(BookshelfUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(WaitingUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(ScoresUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(EndingTokenUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(ChatUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(StartTurnUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(EndGameUpdate update) {
        sendPrivate(update);
    }

    @Override
    public void send(GamesList gamesList) {
        sendPrivate(gamesList);
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        sendPrivate(itemsSelected);
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        sendPrivate(chatAccepted);
    }

    @Override
    public void send(GameData gameData) {
        sendPrivate(gameData);
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        sendPrivate(acceptedInsertion);
    }
}
