package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing a TCP connection (with sockets) to (and from) a client.
 * The send method guarantee thread-safety.
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
     * Flag indicating whether the server needs to disconnect from the client. The lock on this object
     * is needed to access {@code toDisconnect} and {@code clientBeep} and to send messages.
     */
    private boolean disconnect;

    /**
     * Object used for synchronization purposes.
     */
    private final Object internalLock;

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
        disconnect = false;
        internalLock = new Object();
    }

    /**
     * {@inheritDoc}
     * Gets input stream from the {@code socket} and keeps listening for incoming messages.
     */
    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            scheduleTimer();

            while (true) {
                synchronized (internalLock) {
                    if (disconnect) return;
                }

                Object input = in.readObject();
                receive(input);
            }
        } catch (IOException | ClassNotFoundException e) {
            synchronized (internalLock) {
                disconnect = true;
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
     * Analyzes the runtime type of {@code input} and acts appropriately:
     * either the appropriate method of the listener is called, or the {@code Beep}
     * from client is stored in {@code this} and one is sent to the client by {@code this}.
     * @param input the object to analyze (received from the client).
     */
    private void receive(Object input) {
        if (input instanceof Nickname nickname) {
            listener.login(nickname);
        } else if (input instanceof GameInitialization gameInitialization) {
            listener.createGame(gameInitialization);
        } else if (input instanceof GameSelection gameSelection) {
            listener.selectGame(gameSelection);
        } else if (input instanceof LivingRoomSelection livingRoomSelection) {
            listener.selectFromLivingRoom(livingRoomSelection);
        } else if (input instanceof BookshelfInsertion bookshelfInsertion) {
            listener.insertInBookshelf(bookshelfInsertion);
        } else if (input instanceof ChatMessage chatMessage) {
            listener.writeChat(chatMessage);
        } else if (input instanceof Beep beep) {
            synchronized (internalLock) {
                clientBeep = beep;
            }
            sendPrivate(new Beep());
        }
    }

    /**
     * Sends a generic {@code Message} to the client or sets {@code toDisconnect}.
     * It locks on {@code disconnectLock}.
     * @param message the message to send to the client.
     */
    private void sendPrivate(Message message) {
        synchronized (internalLock) {
            if (disconnect) return;
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                disconnect = true;
            }
        }
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
     * {@inheritDoc}
     */
    @Override
    public void start() {
        (new Thread(this)).start();
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
                synchronized (internalLock) {
                    if (clientBeep != null && !disconnect) {
                        clientBeep = null;
                        return;
                    }

                    disconnect = true;

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
