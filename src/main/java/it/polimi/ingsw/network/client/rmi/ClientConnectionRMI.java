package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientUpdateViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class ClientConnectionRMI extends UnicastRemoteObject implements ClientConnection, ClientConnectionRMIInterface {
    private ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final ClientUpdateViewInterface receiver;
    private final Timer serverTimer;
    private final Timer clientTimer;
    private final Object beepLock;
    private Beep serverBeep;

    public ClientConnectionRMI(ClientUpdateViewInterface receiver) throws RemoteException {
        super();
        this.receiver = receiver;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
        beepLock = new Object();
    }

    public void startTimers() {
        clientTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    serverConnectionRMIInterface.beep(new Beep());
                } catch (RemoteException e) {
                    receiver.onDisconnection();
                }
            }
        }, 1000, 1000);

        clientTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (beepLock) {
                    if (serverBeep != null) {
                        serverBeep = null;
                        return;
                    }
                }
                receiver.onDisconnection();
            }
        }, 2000, 2000);
    }

    public void setServerConnectionRMIInterface(ServerConnectionRMIInterface serverConnectionRMIInterface) {
        this.serverConnectionRMIInterface = serverConnectionRMIInterface;
    }

    @Override
    public void send(Nickname message) {
        try {
            serverConnectionRMIInterface.login(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void send(GameInitialization message) {
        try {
            serverConnectionRMIInterface.createGame(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void send(GameSelection message) {
        try {
            serverConnectionRMIInterface.selectGame(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void send(LivingRoomSelection message) {
        try {
            serverConnectionRMIInterface.selectFromLivingRoom(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void send(BookshelfInsertion message) {
        try {
            serverConnectionRMIInterface.insertInBookshelf(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void send(ChatMessage message) {
        try {
            serverConnectionRMIInterface.writeChat(message);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.onDisconnection();
        }
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) throws RemoteException {
        receiver.onLivingRoomUpdate(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) throws RemoteException {
        receiver.onBookshelfUpdate(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) throws RemoteException {
        receiver.onWaitingUpdate(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) throws RemoteException {
        receiver.onScoresUpdate(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) throws RemoteException {
        receiver.onEndingTokenUpdate(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) throws RemoteException {
        receiver.onCommonGoalCardsUpdate(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) throws RemoteException {
        receiver.onPersonalGoalCardUpdate(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) throws RemoteException {
        receiver.onChatUpdate(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) throws RemoteException {
        receiver.onStartTurnUpdate(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) throws RemoteException {
        receiver.onEndGameUpdate(update);
    }

    @Override
    public void sendListOfGames(GamesList list) throws RemoteException {
        receiver.onGamesList(list);
    }

    @Override
    public void sendItemsSelected(ItemsSelected selected) throws RemoteException {
        receiver.onItemsSelected(selected);
    }

    @Override
    public void sendGameDimensions(GameData gameData) throws RemoteException {
        receiver.onGameData(gameData);
    }

    @Override
    public void sendAcceptedInsertion(AcceptedInsertion acceptedInsertion) throws RemoteException {
        receiver.onAcceptedInsertion(acceptedInsertion);
    }

    @Override
    public void sendChatAccepted(ChatAccepted chatAccepted) throws RemoteException {
        receiver.onChatAccepted(chatAccepted);
    }

    @Override
    public void beep(Beep beep) throws RemoteException {
        synchronized (beepLock) {
            serverBeep = beep;
        }
    }
}
