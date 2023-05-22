package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientUpdateViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientConnectionTCP implements ClientConnection, Runnable {
    private final Socket socket;
    private final ClientUpdateViewInterface receiver;
    private final ObjectOutputStream out;
    private final Timer serverTimer;
    private final Timer clientTimer;
    private final Object beepLock;
    private Beep serverBeep;

    public ClientConnectionTCP(Socket socket, ClientUpdateViewInterface receiver) throws IOException {
        this.socket = socket;
        this.receiver = receiver;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        this.beepLock = new Object();
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void send(Nickname message) {
        sendPrivate(message);
    }

    @Override
    public void send(GameInitialization message) {
        sendPrivate(message);
    }

    @Override
    public void send(GameSelection message) {
        sendPrivate(message);
    }

    @Override
    public void send(LivingRoomSelection message) {
        sendPrivate(message);
    }

    @Override
    public void send(BookshelfInsertion message) {
        sendPrivate(message);
    }

    @Override
    public void send(ChatMessage message) {
        sendPrivate(message);
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
        } else if (object instanceof GameData) {
            receiver.onGameData((GameData) object);
        } else if (object instanceof AcceptedInsertion) {
            receiver.onAcceptedInsertion((AcceptedInsertion) object);
        } else if (object instanceof ChatAccepted) {
            receiver.onChatAccepted((ChatAccepted) object);
        } else if (object instanceof Beep) {
            synchronized (beepLock) {
                serverBeep = (Beep) object;
            }
        }
    }



    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            clientTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendPrivate(new Beep());
                }
            }, 1000, 1000);

            serverTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (beepLock) {
                        if (serverBeep != null) {
                            serverBeep = null;
                            return;
                        }
                    }
                    try {
                        socket.close();
                    } catch (IOException ignored) {}
                    receiver.onDisconnection();
                }
            }, 2000, 2000);

            while (socket.isConnected()) {
                input = in.readObject();
                receive(input);
            }
        } catch (IOException | ClassNotFoundException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }

        serverTimer.cancel();
        clientTimer.cancel();
    }

    private void sendPrivate(Message message) {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                serverTimer.cancel();
                clientTimer.cancel();
                receiver.onDisconnection();
            }
        }
    }
}
