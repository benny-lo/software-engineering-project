package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing a RMI connection to (and from) a client.
 * The send methods guarantee thread-safety.
 * Usage: construct an instance of the class by passing to the constructor the listener. Moreover, call the
 * {@code start} method to start listening for {@code Beep}s from the client and to start sending messages.
 */
public class ServerConnectionRMI extends UnicastRemoteObject implements ServerConnection, ServerConnectionRMIInterface {
    /**
     * Estimate of the Round-Trip-Time for the connection between server and client.
     */
    private static final int RTT = 5000;

    /**
     * Queue that contains all messages that need to be sent to the client.
     */
    private final Queue<Message> sendingQueue;

    /**
     * Listener for messages coming from the client.
     */
    private ServerInputViewInterface listener;

    /**
     * RMI interface provided by the client. Used to send message to the client.
     */
    private final ClientConnectionRMIInterface client;

    /**
     * Timer used for disconnection handling.
     */
    private final Timer clientTimer;

    /**
     * Object used for synchronization purposes.
     */
    private final Object beepLock;

    /**
     * Last {@code Beep} message received from client.
     */
    private Beep clientBeep;

    /**
     * Flag that is set to {@code true} if the rmi connection to the client was lost.
     */
    private boolean disconnected;

    private final Object disconnectedLock;

    /**
     * Constructs a new {@code ServerConnectionRMI}. It sets the clientRMI interface.
     * @param client the clientRMI interface.
     * @throws RemoteException rmi exception. It is the one thrown by the no-args constructor
     * of {@code UnicastRemoteObject}.
     */
    public ServerConnectionRMI(ClientConnectionRMIInterface client) throws RemoteException {
        super();
        this.sendingQueue = new ArrayDeque<>();
        this.client = client;
        this.clientTimer = new Timer();
        this.beepLock = new Object();
        this.disconnected = false;
        this.disconnectedLock = new Object();
    }

    /**
     * Starts the timer that constantly checks whether a {@code Beep} from client has arrived. Moreover, it
     * starts the {@code Thread} that will be listening for messages to send (in the {@code sendingQueue})
     * and will be calling the appropriate clientRMI methods.
     */
    public void start() {
        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (beepLock) {
                    synchronized (disconnectedLock) {
                        if (clientBeep != null && !disconnected) {
                            clientBeep = null;
                            return;
                        }
                        disconnected = true;
                    }
                }

                clientTimer.cancel();
                listener.disconnect();
            }
        }, RTT/2, 2*RTT);

        (new Thread(() -> {
            Message message;
            while (true) {
                synchronized (sendingQueue) {
                    while (sendingQueue.isEmpty()) {
                        try {
                            sendingQueue.wait();
                        } catch (InterruptedException ignored) {}
                    }
                    message = sendingQueue.poll();
                }

                synchronized (disconnectedLock) {
                    if (disconnected) return;
                }

                try {
                    if (message instanceof LivingRoomUpdate) {
                        client.receive((LivingRoomUpdate) message);
                    } else if (message instanceof BookshelfUpdate) {
                        client.receive((BookshelfUpdate) message);
                    } else if (message instanceof WaitingUpdate) {
                        client.receive((WaitingUpdate) message);
                    } else if (message instanceof ScoresUpdate) {
                        client.receive((ScoresUpdate) message);
                    } else if (message instanceof EndingTokenUpdate) {
                        client.receive((EndingTokenUpdate) message);
                    } else if (message instanceof CommonGoalCardsUpdate) {
                        client.receive((CommonGoalCardsUpdate) message);
                    } else if (message instanceof PersonalGoalCardUpdate) {
                        client.receive((PersonalGoalCardUpdate) message);
                    } else if (message instanceof ChatUpdate) {
                        client.receive((ChatUpdate) message);
                    } else if (message instanceof StartTurnUpdate) {
                        client.receive((StartTurnUpdate) message);
                    } else if (message instanceof EndGameUpdate) {
                        client.receive((EndGameUpdate) message);
                    } else if (message instanceof GamesList) {
                        client.receive((GamesList) message);
                    } else if (message instanceof SelectedItems) {
                        client.receive((SelectedItems) message);
                    } else if (message instanceof GameData) {
                        client.receive((GameData) message);
                    } else if (message instanceof AcceptedInsertion) {
                        client.receive((AcceptedInsertion) message);
                    } else if (message instanceof ChatAccepted) {
                        client.receive((ChatAccepted) message);
                    }
                } catch (RemoteException e) {
                    synchronized (disconnectedLock) {
                        disconnected = true;
                    }
                }
            }
        })).start();
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
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void login(Nickname message) throws RemoteException {
        listener.login(message);
    }

    /**
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void createGame(GameInitialization message) throws RemoteException {
        listener.createGame(message);
    }

    /**
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void selectGame(GameSelection message) throws RemoteException {
        listener.selectGame(message);
    }

    /**
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException {
        listener.selectFromLivingRoom(message);
    }

    /**
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void insertInBookshelf(BookshelfInsertion message) throws RemoteException {
        listener.insertInBookshelf(message);
    }

    /**
     * {@inheritDoc}
     * @param message the request from the client.
     * @throws RemoteException
     */
    @Override
    public void writeChat(ChatMessage message) throws RemoteException {
        listener.writeChat(message);
    }

    /**
     * {@inheritDoc}
     * @param beep the beep from the client.
     * @throws RemoteException
     */
    @Override
    public void beep(Beep beep) throws RemoteException {
        synchronized (beepLock) {
            this.clientBeep = beep;
        }

        try {
            client.receive(new Beep());
        } catch (RemoteException e) {
            synchronized (disconnectedLock) {
                disconnected = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(Message message) {
        synchronized (sendingQueue) {
            sendingQueue.add(message);
            sendingQueue.notifyAll();
        }
    }
}
