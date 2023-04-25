package network.server.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.GameInfo;
import it.polimi.ingsw.view.VirtualView;
import network.client.ClientRMIInterface;
import network.server.Lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    private final Lobby lobby;
    private VirtualView view;

    public ServerRMI(Lobby lobby) throws RemoteException {
        this.lobby = lobby;
        this.view = null;
    }

    @Override
    public List<GameInfo> login(ClientRMIInterface client, String nickname) {
        view = new VirtualView(nickname);
        lobby.addVirtualView(view);
        return lobby.getGameInfo();
    }

    @Override
    public boolean selectGame(int id) {
        return false;
    }

    @Override
    public boolean createGame(int numberPlayers, int numberCommonGoals) {
        return false;
    }

    @Override
    public List<Item> selectFromLivingRoom(List<Position> position) throws RemoteException {
        return null;
    }

    @Override
    public boolean putInBookshelf(int column, List<Integer> permutation) {
        return false;
    }
}
