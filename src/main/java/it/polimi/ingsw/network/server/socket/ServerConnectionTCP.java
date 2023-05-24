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
    private final Timer serverTimer;
    private final Timer clientTimer;
    private final Object beepLock;
    private Beep clientBeep;
    private final Socket socket;
    private ServerInputViewInterface receiver;
    private ObjectOutputStream out;

    public ServerConnectionTCP(Socket socket) {
        serverTimer = new Timer();
        clientTimer = new Timer();
        beepLock = new Object();
        this.socket = socket;

        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
        } catch (IOException e) {
            receiver.disconnect();
            return;
        }

        this.out = out;
    }

    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            serverTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendPrivate(new Beep());
                }
            }, 1000, 2000);

            clientTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (beepLock) {
                        if (clientBeep != null) {
                            clientBeep = null;
                            return;
                        }
                    }
                    synchronized (socket) {
                        try {
                            socket.close();
                        } catch (IOException ignored) {
                        }
                    }
                    receiver.disconnect();
                }
            }, 2000, 2000);

            while (true) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException | ClassNotFoundException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
            synchronized (socket) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }

        serverTimer.cancel();
        clientTimer.cancel();
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
            synchronized (beepLock) {
                clientBeep = (Beep) input;
            }
        }
    }

    private synchronized void sendPrivate(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
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
        synchronized (socket) {
            try {
                socket.close();
            } catch (IOException ignored) {}
            receiver.disconnect();
        }
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
