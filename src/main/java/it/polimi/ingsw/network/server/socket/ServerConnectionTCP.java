package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ServerConnectionTCP implements ServerConnection, Runnable {
    private static final int RTT = 5000;
    private final Timer clientTimer;
    private Beep clientBeep;
    private final Socket socket;
    private ServerInputViewInterface receiver;
    private final ObjectOutputStream out;
    private boolean toDisconnect;
    private final Object disconnectLock;

    public ServerConnectionTCP(Socket socket) throws IOException {
        clientTimer = new Timer();
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        toDisconnect = false;
        disconnectLock = new Object();
    }

    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            scheduleTimer();

            while (true) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException | ClassNotFoundException e) {
            synchronized (disconnectLock) {
                toDisconnect = true;
            }
        }
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface receiver) {
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
        } else if (input instanceof Beep) {
            synchronized (disconnectLock) {
                clientBeep = (Beep) input;
            }
            sendPrivate(new Beep());
        }
    }

    private void sendPrivate(Message message) {
        synchronized (disconnectLock) {
            if (toDisconnect) return;
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                toDisconnect = true;
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
    public void send(SelectedItems selectedItems) {
        sendPrivate(selectedItems);
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

    private void scheduleTimer() {
        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (disconnectLock) {
                    if (clientBeep != null && !toDisconnect) {
                        clientBeep = null;
                        return;
                    }
                }

                toDisconnect = true;

                try {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    socket.close();
                } catch (IOException ignored) {}

                clientTimer.cancel();
                receiver.disconnect();
            }
        }, RTT/2, 2*RTT);
    }
}
