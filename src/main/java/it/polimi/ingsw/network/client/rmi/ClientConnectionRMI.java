package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.UpdateViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientConnectionRMI extends UnicastRemoteObject implements ClientConnection, ClientConnectionRMIInterface {
    private ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final UpdateViewInterface updateReceiver;

    public ClientConnectionRMI(UpdateViewInterface updateReceiver) throws RemoteException {
        super();
        this.updateReceiver = updateReceiver;
    }

    public void setServerConnectionRMIInterface(ServerConnectionRMIInterface serverConnectionRMIInterface) {
        this.serverConnectionRMIInterface = serverConnectionRMIInterface;
    }

    @Override
    public void login(Nickname message) {
        try {
            serverConnectionRMIInterface.login(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 29: failed login");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(GameInitialization message) {
        try {
            serverConnectionRMIInterface.createGame(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 39: failed createGame");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectGame(GameSelection message) {
        try {
            serverConnectionRMIInterface.selectGame(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 49: failed selectGame");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        try {
            serverConnectionRMIInterface.selectFromLivingRoom(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 59: failed selectFromLivingRoom");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        try {
            serverConnectionRMIInterface.insertInBookshelf(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 69: failed insertInBookshelf");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeChat(ChatMessage message) {
        try {
            serverConnectionRMIInterface.writeChat(message);
        } catch (RemoteException e) {
            System.err.println("ClientConnectionTCP, line 79: failed writeChat");
        }
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) throws RemoteException {
        updateReceiver.onLivingRoomUpdate(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) throws RemoteException {
        updateReceiver.onBookshelfUpdate(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) throws RemoteException {
        updateReceiver.onWaitingUpdate(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) throws RemoteException {
        updateReceiver.onScoresUpdate(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) throws RemoteException {
        updateReceiver.onEndingTokenUpdate(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) throws RemoteException {
        updateReceiver.onCommonGoalCardsUpdate(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) throws RemoteException {
        updateReceiver.onPersonalGoalCardUpdate(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) throws RemoteException {
        updateReceiver.onChatUpdate(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) throws RemoteException {
        updateReceiver.onStartTurnUpdate(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) throws RemoteException {
        updateReceiver.onEndGameUpdate(update);
    }

    @Override
    public void sendListOfGames(GamesList list) throws RemoteException {
        updateReceiver.onGamesList(list);
    }

    @Override
    public void sendItemsSelected(ItemsSelected selected) throws RemoteException {
        updateReceiver.onItemsSelected(selected);
    }

    @Override
    public void sendAcceptedAction(AcceptedAction accepted) throws RemoteException {
        updateReceiver.onAcceptedAction(accepted);
    }
}
