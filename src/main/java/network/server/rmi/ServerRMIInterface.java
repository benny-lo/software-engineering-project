package network.server.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.GameInfo;
import network.client.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerRMIInterface extends Remote {
    List<GameInfo> login(String nickname, ClientRMIInterface clientRMIInterface) throws RemoteException;
    boolean selectGame(String nickname, int id) throws RemoteException;
    boolean createGame(String nickname, int numberPlayers, int numberCommonGoals) throws RemoteException;
    List<Item> selectFromLivingRoom(String nickname, List<Position> position) throws RemoteException;
    boolean putInBookshelf(String nickname, int column, List<Integer> permutation) throws RemoteException;
}
