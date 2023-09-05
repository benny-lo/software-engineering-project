package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.UpdateViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Class representing an RMI connection on the client-side to the server.
 */
public class ClientConnectionRMI extends UnicastRemoteObject implements ClientConnection, ClientConnectionRMIInterface {
    private static final int RTT = 5000;
    private transient ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final transient UpdateViewInterface listener;
    private final transient Timer serverTimer;
    private final transient Timer clientTimer;
    private final transient Object internalLock;
    private transient boolean disconnected;
    private final transient Queue<Message> sendingQueue;
    private transient Beep serverBeep;

    /**
     * Constructs a new {@code ClientConnectionRMI}. It sets the listener of the network.
     * @param listener The listener of the network.
     * @throws RemoteException Rmi exception. It is the one thrown by the no-args constructor
     * of {@code UnicastRemoteObject}.
     */
    public ClientConnectionRMI(UpdateViewInterface listener) throws RemoteException {
        super();
        this.listener = listener;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        this.sendingQueue = new ArrayDeque<>();
        this.disconnected = false;
        this.internalLock = new Object();
    }

    /**
     * Schedules the timers to send and check for {@code Beep}s (heartbeats) periodically.
     */
    public void scheduleTimers() {
        (new Thread(() -> {
            Message message;
            while (true) {
                synchronized (sendingQueue) {
                    while (sendingQueue.isEmpty()) {
                        try {
                            sendingQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    message = sendingQueue.poll();
                }

                synchronized (internalLock) {
                    if (disconnected) return;
                }

                try {
                    if (message instanceof BookshelfInsertion m) {
                        serverConnectionRMIInterface.insertInBookshelf(m);
                    } else if (message instanceof ChatMessage m) {
                        serverConnectionRMIInterface.writeChat(m);
                    } else if (message instanceof GameInitialization m) {
                        serverConnectionRMIInterface.createGame(m);
                    } else if (message instanceof GameSelection m) {
                        serverConnectionRMIInterface.selectGame(m);
                    } else if (message instanceof LivingRoomSelection m) {
                        serverConnectionRMIInterface.selectFromLivingRoom(m);
                    } else if (message instanceof Nickname m) {
                        serverConnectionRMIInterface.login(m);
                    }
                } catch (RemoteException e) {
                    synchronized (internalLock) {
                        disconnected = true;
                    }
                }
            }
        })).start();

        serverTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (internalLock) {
                    if (serverBeep != null && !disconnected) {
                        serverBeep = null;
                        return;
                    }

                    disconnected = true;
                }

                serverTimer.cancel();
                clientTimer.cancel();
                listener.onDisconnectionUpdate(null);
            }
        }, RTT, 2*RTT);

        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (internalLock) {
                    if (disconnected) return;
                }

                try {
                    serverConnectionRMIInterface.beep(new Beep());
                } catch (RemoteException e) {
                    synchronized (internalLock) {
                        disconnected = true;
                    }
                }
            }
        }, 0, 2*RTT);
    }

    /**
     * Sets the server RMI interface.
     * @param serverConnectionRMIInterface The server RMI interface.
     */
    public void setServerConnectionRMIInterface(ServerConnectionRMIInterface serverConnectionRMIInterface) {
        this.serverConnectionRMIInterface = serverConnectionRMIInterface;
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

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(LivingRoomUpdate update) throws RemoteException {
        listener.onLivingRoomUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(BookshelfUpdate update) throws RemoteException {
        listener.onBookshelfUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(WaitingUpdate update) throws RemoteException {
        listener.onWaitingUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(ScoresUpdate update) throws RemoteException {
        listener.onScoresUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(EndingTokenUpdate update) throws RemoteException {
        listener.onEndingTokenUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(CommonGoalCardsUpdate update) throws RemoteException {
        listener.onCommonGoalCardsUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(PersonalGoalCardUpdate update) throws RemoteException {
        listener.onPersonalGoalCardUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(ChatUpdate update) throws RemoteException {
        listener.onChatUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(StartTurnUpdate update) throws RemoteException {
        listener.onStartTurnUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(EndGameUpdate update) throws RemoteException {
        listener.onEndGameUpdate(update);
    }

    /**
     * {@inheritDoc}
     * @param list The {@code GamesList} to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(GamesList list) throws RemoteException {
        listener.onGamesList(list);
    }

    /**
     * {@inheritDoc}
     * @param selected The {@code SelectedItems} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(SelectedItems selected) throws RemoteException {
        listener.onSelectedItems(selected);
    }

    /**
     * {@inheritDoc}
     * @param gameData The {@code GameData} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(GameData gameData) throws RemoteException {
        listener.onGameData(gameData);
    }

    /**
     * {@inheritDoc}
     * @param acceptedInsertion The {@code AcceptedInsertion} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(AcceptedInsertion acceptedInsertion) throws RemoteException {
        listener.onAcceptedInsertion(acceptedInsertion);
    }

    /**
     * {@inheritDoc}
     * @param chatAccepted The {@code ChatAccepted} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(ChatAccepted chatAccepted) throws RemoteException {
        listener.onChatAccepted(chatAccepted);
    }

    @Override
    public void receive(Disconnection disconnection) throws RemoteException {
        listener.onDisconnectionUpdate(disconnection);
    }

    @Override
    public void receive(Reconnection reconnection) throws RemoteException {
        listener.onReconnectionUpdate(reconnection);
    }

    /**
     * {@inheritDoc}
     * @param beep The {@code Beep} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(Beep beep) throws RemoteException {
        synchronized (internalLock) {
            serverBeep = beep;
        }
    }
}
