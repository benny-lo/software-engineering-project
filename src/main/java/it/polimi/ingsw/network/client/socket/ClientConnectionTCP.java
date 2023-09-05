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
        if (input instanceof GamesList gamesList) {
            listener.onGamesList(gamesList);
        } else if (input instanceof SelectedItems selectedItems) {
            listener.onSelectedItems(selectedItems);
        } else if (input instanceof LivingRoomUpdate livingRoomUpdate) {
            listener.onLivingRoomUpdate(livingRoomUpdate);
        } else if (input instanceof BookshelfUpdate bookshelfUpdate) {
            listener.onBookshelfUpdate(bookshelfUpdate);
        } else if (input instanceof EndingTokenUpdate endingTokenUpdate) {
            listener.onEndingTokenUpdate(endingTokenUpdate);
        } else if (input instanceof WaitingUpdate waitingUpdate) {
            listener.onWaitingUpdate(waitingUpdate);
        } else if (input instanceof ScoresUpdate scoresUpdate) {
            listener.onScoresUpdate(scoresUpdate);
        } else if (input instanceof CommonGoalCardsUpdate commonGoalCardsUpdate) {
            listener.onCommonGoalCardsUpdate(commonGoalCardsUpdate);
        } else if (input instanceof PersonalGoalCardUpdate personalGoalCardUpdate) {
            listener.onPersonalGoalCardUpdate(personalGoalCardUpdate);
        } else if (input instanceof ChatUpdate chatUpdate) {
            listener.onChatUpdate(chatUpdate);
        } else if (input instanceof StartTurnUpdate startTurnUpdate) {
            listener.onStartTurnUpdate(startTurnUpdate);
        } else if (input instanceof EndGameUpdate endGameUpdate) {
            listener.onEndGameUpdate(endGameUpdate);
        } else if (input instanceof GameData gameData) {
            listener.onGameData(gameData);
        } else if (input instanceof AcceptedInsertion acceptedInsertion) {
            listener.onAcceptedInsertion(acceptedInsertion);
        } else if (input instanceof ChatAccepted chatAccepted) {
            listener.onChatAccepted(chatAccepted);
        } else if (input instanceof Disconnection disconnection) {
            listener.onDisconnectionUpdate(disconnection);
        } else if (input instanceof Reconnection reconnection) {
            listener.onReconnectionUpdate(reconnection);
        } else if (input instanceof Beep beep) {
            synchronized (internalLock) {
                serverBeep = beep;
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
