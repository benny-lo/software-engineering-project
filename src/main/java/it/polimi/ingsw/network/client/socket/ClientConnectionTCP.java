package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.UpdateViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing a TCP (with sockets) connection to and from the server.
 * The send methods guarantee thread-safety.
 */
public class ClientConnectionTCP implements ClientConnection, Runnable {
    /**
     * Estimate of the Round-Trip-Time of the connection.
     */
    private static final int RTT = 5000;

    /**
     * The {@code Socket} used to communicate with the server.
     */
    private final Socket socket;

    /**
     * The listener for messages coming from the server.
     */
    private final UpdateViewInterface listener;

    /**
     * The output stream used to send messages to the server.
     */
    private final ObjectOutputStream out;

    /**
     * Timer scheduling a task to check for {@code Beep} from the server.
     */
    private final Timer serverTimer;

    /**
     * Timer scheduling a task to send constantly {@code Beep}s to the server.
     */
    private final Timer clientTimer;

    /**
     * Object used for synchronization purposes. The lock on this object is required to
     * access {@code toDisconnect} and {@code serverBeep} and to send messages.
     */
    private final Object internalLock;

    /**
     * Flag indicating whether the connection is set to disconnect.
     */
    private boolean disconnect;

    /**
     * The last {@code Beep} got from the server.
     */
    private Beep serverBeep;

    /**
     * Constructs a {@code ClientConnectionTCP} from a {@code Socket} object. It initializes the
     * output stream of objects, moreover it initializes {@code toDisconnect} to {@code false}. Finally,
     * it also sets the {@code listener}.
     * @param socket the socket to use.
     * @param listener the listener of the connection.
     * @throws IOException exception relayed from the opening of a {@code ObjectOutputStream} from
     * the output stream of the {@code socket}.
     */
    public ClientConnectionTCP(Socket socket, UpdateViewInterface listener) throws IOException {
        this.socket = socket;
        this.listener = listener;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        this.internalLock = new Object();
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.disconnect = false;
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        sendPrivate(message);
    }

    /**
     * Analyzes the runtime type of {@code input} and acts appropriately:
     * either the appropriate method of the listener is called, or the {@code Beep}
     * from client is stored in {@code this}.
     * @param input the object to analyze (received from the server).
     */
    private void receive(Object input) {
        if (input instanceof GamesList) {
            listener.onGamesList((GamesList) input);
        } else if (input instanceof SelectedItems) {
            listener.onSelectedItems((SelectedItems) input);
        } else if (input instanceof LivingRoomUpdate) {
            listener.onLivingRoomUpdate((LivingRoomUpdate) input);
        } else if (input instanceof BookshelfUpdate) {
            listener.onBookshelfUpdate((BookshelfUpdate) input);
        } else if (input instanceof EndingTokenUpdate) {
            listener.onEndingTokenUpdate((EndingTokenUpdate) input);
        } else if (input instanceof WaitingUpdate) {
            listener.onWaitingUpdate((WaitingUpdate) input);
        } else if (input instanceof ScoresUpdate) {
            listener.onScoresUpdate((ScoresUpdate) input);
        } else if (input instanceof CommonGoalCardsUpdate) {
            listener.onCommonGoalCardsUpdate((CommonGoalCardsUpdate) input);
        } else if (input instanceof PersonalGoalCardUpdate) {
            listener.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) input);
        } else if (input instanceof ChatUpdate) {
            listener.onChatUpdate((ChatUpdate) input);
        } else if (input instanceof StartTurnUpdate) {
            listener.onStartTurnUpdate((StartTurnUpdate) input);
        } else if (input instanceof EndGameUpdate) {
            listener.onEndGameUpdate((EndGameUpdate) input);
        } else if (input instanceof GameData) {
            listener.onGameData((GameData) input);
        } else if (input instanceof AcceptedInsertion) {
            listener.onAcceptedInsertion((AcceptedInsertion) input);
        } else if (input instanceof ChatAccepted) {
            listener.onChatAccepted((ChatAccepted) input);
        } else if (input instanceof Disconnection m) {
            listener.onDisconnectionUpdate(m);
        } else if (input instanceof Reconnection m) {
            listener.onReconnectionUpdate(m);
        } else if (input instanceof Beep) {
            synchronized (internalLock) {
                serverBeep = (Beep) input;
            }
        }
    }

    /**
     * {@inheritDoc}
     * Gets input stream from the {@code socket} and keeps listening for incoming messages.
     */
    @Override
    public void run() {
        try {
            Object input;
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            scheduleTimers();

            while (true) {
                synchronized (internalLock) {
                    if (disconnect) return;
                }

                input = in.readObject();
                receive(input);
            }
        } catch (IOException | ClassNotFoundException ignored) {
            synchronized (internalLock) {
                disconnect = true;
            }
        }
    }

    /**
     * Sends a generic {@code Message} to the server.
     * It locks on {@code disconnectLock}.
     * @param message the message to send to the server.
     */
    private void sendPrivate(Message message) {
        synchronized (internalLock) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException ignored) {
                disconnect = true;
            }
        }
    }

    /**
     * Schedules the {@code serverTimer} to check constantly whether a {@code Beep} was received.
     * Schedules the {@code clientTimer} to send {@code Beep}s to the server.
     */
    private void scheduleTimers() {
        serverTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (internalLock) {
                    if (serverBeep != null && !disconnect) {
                        serverBeep = null;
                        return;
                    }

                    disconnect = true;

                    try {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException ignored) {}
                }

                serverTimer.cancel();
                clientTimer.cancel();
                listener.onDisconnectionUpdate(new Disconnection(null));
            }
        }, RTT, 2*RTT);
        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendPrivate(new Beep());
            }
        }, 0, 2*RTT);
    }
}
