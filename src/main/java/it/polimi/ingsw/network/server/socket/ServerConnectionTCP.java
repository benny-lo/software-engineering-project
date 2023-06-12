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

/**
 * Class representing a TCP connection (with sockets) to (and from) a client.
 * The send methods guarantee thread-safety.
 * Usage: after constructing an instance of the class, set the listener and start thread with {@code this}
 * to listen for messages from the client.
 */
public class ServerConnectionTCP implements ServerConnection, Runnable {
    /**
     * Estimated Round-Trip-Time of the connection with the client.
     */
    private static final int RTT = 5000;

    /**
     * Timer to deal with disconnections.
     */
    private final Timer clientTimer;

    /**
     * Latest {@code Beep} message received from client.
     */
    private Beep clientBeep;

    /**
     * The socket used to communicate with the client. (It used to get input and output streams).
     */
    private final Socket socket;

    /**
     * The listener of the connection for messages from client.
     */
    private ServerInputViewInterface listener;

    /**
     * Output stream of objects got from the {@code socket}.
     */
    private final ObjectOutputStream out;

    /**
     * Flag indicating whether the server needs to disconnect from the client.
     */
    private boolean toDisconnect;

    /**
     * Object used for synchronization purposes.
     */
    private final Object disconnectLock;

    /**
     * Constructs a {@code ServerConnectionTCP} from a {@code Socket} object. It initializes the
     * output stream of objects, moreover it initializes {@code toDisconnect} to {@code false}.
     * @param socket the socket to use.
     * @throws IOException exception relayed from the opening of a {@code ObjectOutputStream} from
     * the output stream of the {@code socket}.
     */
    public ServerConnectionTCP(Socket socket) throws IOException {
        clientTimer = new Timer();
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        toDisconnect = false;
        disconnectLock = new Object();
    }

    /**
     * {@inheritDoc}
     * Get input stream from the {@code socket} and keep listening for incoming messages.
     */
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

    /**
     * {@inheritDoc}
     * @param receiver the listener.
     */
    @Override
    public void setServerInputViewInterface(ServerInputViewInterface receiver) {
        this.listener = receiver;
    }

    /**
     * Analyzes the runtime type of {@code} and acts appropriately:
     * either the appropriate method of the listener is called, or the {@code Beep}
     * from client is stored in {@code this} and one is sent to the client by {@code this}.
     * @param input the object to analyze (received from client).
     */
    private void receive(Object input) {
        if (input instanceof Nickname) {
            listener.login((Nickname) input);
        } else if (input instanceof GameInitialization) {
            listener.createGame((GameInitialization) input);
        } else if (input instanceof GameSelection) {
            listener.selectGame((GameSelection) input);
        } else if (input instanceof LivingRoomSelection) {
            listener.selectFromLivingRoom((LivingRoomSelection) input);
        } else if (input instanceof BookshelfInsertion) {
            listener.insertInBookshelf((BookshelfInsertion) input);
        } else if (input instanceof ChatMessage) {
            listener.writeChat((ChatMessage) input);
        } else if (input instanceof Beep) {
            synchronized (disconnectLock) {
                clientBeep = (Beep) input;
            }
            sendPrivate(new Beep());
        }
    }

    /**
     * Sends a generic {@code Message} to client or sets {@code toDisconnect}.
     * It locks on {@code disconnectLock}.
     * @param message the message to send to client.
     */
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

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(LivingRoomUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(BookshelfUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(WaitingUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(ScoresUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(EndingTokenUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(CommonGoalCardsUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(PersonalGoalCardUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(ChatUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(StartTurnUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param update the update to send.
     */
    @Override
    public void send(EndGameUpdate update) {
        sendPrivate(update);
    }

    /**
     * {@inheritDoc}
     * @param gamesList the message to send.
     */
    @Override
    public void send(GamesList gamesList) {
        sendPrivate(gamesList);
    }

    /**
     * {@inheritDoc}
     * @param selectedItems the message to send.
     */
    @Override
    public void send(SelectedItems selectedItems) {
        sendPrivate(selectedItems);
    }

    /**
     * {@inheritDoc}
     * @param chatAccepted the message to send.
     */
    @Override
    public void send(ChatAccepted chatAccepted) {
        sendPrivate(chatAccepted);
    }

    /**
     * {@inheritDoc}
     * @param gameData the message to send.
     */
    @Override
    public void send(GameData gameData) {
        sendPrivate(gameData);
    }

    /**
     * {@inheritDoc}
     * @param acceptedInsertion the message to send.
     */
    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        sendPrivate(acceptedInsertion);
    }

    /**
     * Schedules the {@code clientTimer} to check constantly whether a {@code Beep} was received.
     * If no {@code Beep} was received, the timer closes the {@code socket} and shutdowns the streams,
     * cancels itself, and notifies the listener of the disconnection.
     */
    private void scheduleTimer() {
        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (disconnectLock) {
                    if (clientBeep != null && !toDisconnect) {
                        clientBeep = null;
                        return;
                    }

                    toDisconnect = true;

                    try {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException ignored) {}
                }

                clientTimer.cancel();
                listener.disconnect();
            }
        }, RTT/2, 2*RTT);
    }
}
