package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.ClientView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RequestSenderRMI extends UnicastRemoteObject implements RequestSender, ClientConnectionRMIInterface {
    private final ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final ClientView updateReceiver;

    public RequestSenderRMI(ServerConnectionRMIInterface serverConnectionRMIInterface, ClientView updateReceiver) throws RemoteException {
        super();
        this.serverConnectionRMIInterface = serverConnectionRMIInterface;
        this.updateReceiver = updateReceiver;
    }

    @Override
    public void login(String nickname) {
        try {
            serverConnectionRMIInterface.login(nickname, this);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#login");
            e.printStackTrace();
        }
    }

    @Override
    public void selectGame(String nickname, int id) {
        try {
            serverConnectionRMIInterface.selectGame(nickname, id);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#selectGame");
            e.printStackTrace();
        }
    }

    @Override
    public void createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        try {
            serverConnectionRMIInterface.createGame(nickname, numberPlayers, numberCommonGoals);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#createGame");
            e.printStackTrace();
        }
    }

    @Override
    public void selectFromLivingRoom(String nickname, List<Position> position) {
        try {
            serverConnectionRMIInterface.selectFromLivingRoom(nickname, position);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#selectFromLivingRoom");
            e.printStackTrace();
        }
    }

    @Override
    public void putInBookshelf(String nickname, int column, List<Integer> permutation) {
        try {
            serverConnectionRMIInterface.putInBookshelf(nickname, column, permutation);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#putInBookshelf");
            e.printStackTrace();
        }
    }

    @Override
    public void addMessage(String nickname, String text) {
        try {
            serverConnectionRMIInterface.addMessage(nickname, text);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#addMessage");
            e.printStackTrace();
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
        updateReceiver.onCommonGoalCardUpdate(update);
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
