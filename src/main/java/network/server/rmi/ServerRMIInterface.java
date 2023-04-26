package network.server.rmi;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.GameInfo;
import network.client.ClientRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerRMIInterface extends Remote {
    List<GameInfo> login(ClientRMIInterface client, String nickname) throws RemoteException;
    boolean selectGame(int id) throws RemoteException;
    boolean createGame(int numberPlayers, int numberCommonGoals) throws RemoteException;
    boolean selectFromLivingRoom(List<Position> position) throws RemoteException;
    boolean putInBookshelf(int column, List<Integer> permutation) throws RemoteException;
}
