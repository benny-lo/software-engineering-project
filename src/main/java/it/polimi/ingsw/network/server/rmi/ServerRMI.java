package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.utils.GameInfo;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.network.client.ClientRMIInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    private final Lobby lobby;

    public ServerRMI(Lobby lobby) throws RemoteException {
        this.lobby = lobby;
    }

    @Override
    public List<GameInfo> login(String nickname, ClientRMIInterface clientRMIInterface) throws RemoteException {
        if(lobby.getView(nickname) != null) {
            return null;
            // TODO: consider the case in which view is present because client was disconnected and is reconnecting.
        }
        VirtualView view = new VirtualView(nickname);
        view.setToClient(clientRMIInterface);
        lobby.addVirtualView(view);
        return lobby.getGameInfo();
    }

    @Override
    public boolean selectGame(String nickname, int id) throws RemoteException {
        VirtualView view = lobby.getView(nickname);
        if (view == null) return false;

        if (!lobby.getControllers().containsKey(id)) return false;

        lobby.getControllers().get(id).update(new JoinAction(nickname, lobby.getView(nickname)));
        boolean error = view.isError();
        if (!error) {
            view.setController(lobby.getControllers().get(id));
        }
        return !view.isError();
    }

    @Override
    public boolean createGame(String nickname, int numberPlayers, int numberCommonGoals) throws RemoteException {
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2)
            return false;

        VirtualView view = lobby.getView(nickname);
        if (view == null) return false;

        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.update(new JoinAction(nickname, view));

        if (view.isError()) return false;

        view.setController(controller);
        lobby.addController(controller);
        return true;
    }

    @Override
    public List<Item> selectFromLivingRoom(String nickname, List<Position> positions) throws RemoteException {
        VirtualView view = lobby.getView(nickname);
        if (view == null) return null;

        Controller controller = view.getController();
        if (controller == null) return null;

        controller.update(new SelectionFromLivingRoomAction(nickname, positions));

        if (view.isError()) return null;

        return view.getItemsChosenRep().getItemsChosen();
    }

    @Override
    public boolean putInBookshelf(String nickname, int column, List<Integer> permutation) throws RemoteException {
        VirtualView view = lobby.getView(nickname);
        if (view == null) return false;

        Controller controller = view.getController();
        if (controller == null) return false;

        controller.update(new SelectionColumnAndOrderAction(nickname, column, permutation));

        return !view.isError();
    }

    @Override
    public boolean addMessage(String nickname, String text) throws RemoteException {
        VirtualView view = lobby.getView(nickname);
        if (view == null) return false;

        Controller controller = view.getController();
        if (controller == null) return false;

        controller.update(new ChatMessageAction(nickname, text));

        return !view.isError();
    }
}
