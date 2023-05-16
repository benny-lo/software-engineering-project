package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerConnectionRMIInterface extends Remote {
    void login(String nickname, ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException;
    void selectGame(String nickname, int id) throws RemoteException;
    void createGame(String nickname, int numberPlayers, int numberCommonGoals) throws RemoteException;
    void selectFromLivingRoom(String nickname, List<Position> position) throws RemoteException;
    void putInBookshelf(String nickname, int column, List<Integer> permutation) throws RemoteException;
    void addMessage(String nickname, String text) throws RemoteException;
}
