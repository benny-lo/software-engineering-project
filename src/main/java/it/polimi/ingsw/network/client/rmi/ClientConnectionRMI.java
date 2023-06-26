package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientUpdateViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class ClientConnectionRMI extends UnicastRemoteObject implements ClientConnection, ClientConnectionRMIInterface {
    private static final int RTT = 5000;
    private ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final ClientUpdateViewInterface listener;
    private final Timer serverTimer;
    private final Timer clientTimer;
    private final Object beepLock;
    private Beep serverBeep;

    /**
     * Constructs a new {@code ClientConnectionRMI}. It sets the listener of the network.
     * @param listener The listener of the network.
     * @throws RemoteException Rmi exception. It is the one thrown by the no-args constructor
     * of {@code UnicastRemoteObject}.
     */
    public ClientConnectionRMI(ClientUpdateViewInterface listener) throws RemoteException {
        super();
        this.listener = listener;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        beepLock = new Object();
    }

    /**
     * Schedules the timers to send and check for {@code Beep}s (heartbeats) periodically.
     */
    public void scheduleTimers() {
        serverTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (beepLock) {
                    if (serverBeep != null) {
                        serverBeep = null;
                        return;
                    }
                }

                serverTimer.cancel();
                clientTimer.cancel();
                listener.onDisconnection();
            }
        }, RTT, 2*RTT);

        clientTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    serverConnectionRMIInterface.beep(new Beep());
                } catch (RemoteException e) {
                    serverTimer.cancel();
                    clientTimer.cancel();
                    listener.onDisconnection();
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
    public void send(Nickname message) {
        try {
            serverConnectionRMIInterface.login(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(GameInitialization message) {
        try {
            serverConnectionRMIInterface.createGame(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(GameSelection message) {
        try {
            serverConnectionRMIInterface.selectGame(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(LivingRoomSelection message) {
        try {
            serverConnectionRMIInterface.selectFromLivingRoom(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(BookshelfInsertion message) {
        try {
            serverConnectionRMIInterface.insertInBookshelf(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
        }
    }

    /**
     * {@inheritDoc}
     * @param message the message to send.
     */
    @Override
    public void send(ChatMessage message) {
        try {
            serverConnectionRMIInterface.writeChat(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            listener.onDisconnection();
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

    /**
     * {@inheritDoc}
     * @param beep The {@code Beep} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public void receive(Beep beep) throws RemoteException {
        synchronized (beepLock) {
            serverBeep = beep;
        }
    }
}
