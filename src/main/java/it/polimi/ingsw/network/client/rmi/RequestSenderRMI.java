package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.ClientView;

import java.rmi.RemoteException;
import java.util.List;

public class RequestSenderRMI implements RequestSender, ClientConnectionRMIInterface {
    private final ServerConnectionRMIInterface serverConnectionRMIInterface;
    private final ClientView view;

    public RequestSenderRMI(ServerConnectionRMIInterface serverConnectionRMIInterface, ClientView view) {
        this.serverConnectionRMIInterface = serverConnectionRMIInterface;
        this.view = view;
    }

    @Override
    public void login(String nickname) {
        List<GameInfo> ret = null;
        try {
            ret = serverConnectionRMIInterface.login(nickname, this);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#login");
            e.printStackTrace();
        }

        view.onGamesList(new GamesList(ret));
    }

    @Override
    public void selectGame(String nickname, int id) {
        boolean ret = false;
        try {
            ret = serverConnectionRMIInterface.selectGame(nickname, id);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#selectGame");
            e.printStackTrace();
        }

        view.onAcceptedAction(new AcceptedAction(ret, "GAME_SELECTION"));
    }

    @Override
    public void createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        boolean ret = false;
        try {
            ret = serverConnectionRMIInterface.createGame(nickname, numberPlayers, numberCommonGoals);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#createGame");
            e.printStackTrace();
        }

        view.onAcceptedAction(new AcceptedAction(ret, "GAME_INITIALIZATION"));
    }

    @Override
    public void selectFromLivingRoom(String nickname, List<Position> position) {
        List<Item> ret = null;
        try {
            ret = serverConnectionRMIInterface.selectFromLivingRoom(nickname, position);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#selectFromLivingRoom");
            e.printStackTrace();
        }

        view.onItemsSelected(new ItemsSelected(ret));
    }

    @Override
    public void putInBookshelf(String nickname, int column, List<Integer> permutation) {
        boolean ret = false;
        try {
            ret = serverConnectionRMIInterface.putInBookshelf(nickname, column, permutation);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#putInBookshelf");
            e.printStackTrace();
        }

        view.onAcceptedAction(new AcceptedAction(ret, "BOOKSHELF_INSERTION"));
    }

    @Override
    public void addMessage(String nickname, String text) {
        boolean ret = false;
        try {
            ret = serverConnectionRMIInterface.addMessage(nickname, text);
        } catch (RemoteException e) {
            System.err.println("error in RequestSenderRMI#addMessage");
            e.printStackTrace();
        }

        view.onAcceptedAction(new AcceptedAction(ret, "CHAT_WRITE"));
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) throws RemoteException {
        view.onLivingRoomUpdate(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) throws RemoteException {
        view.onBookshelfUpdate(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) throws RemoteException {
        view.onWaitingUpdate(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) throws RemoteException {
        view.onScoresUpdate(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) throws RemoteException {
        view.onEndingTokenUpdate(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) throws RemoteException {
        view.onCommonGoalCardUpdate(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) throws RemoteException {
        view.onPersonalGoalCardUpdate(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) throws RemoteException {
        view.onChatUpdate(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) throws RemoteException {
        view.onStartTurnUpdate(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) throws RemoteException {
        view.onEndGameUpdate(update);
    }
}
