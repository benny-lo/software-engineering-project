package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.utils.networkMessage.client.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerConnectionRMIInterface extends Remote {
    void login(Nickname message) throws RemoteException;
    void createGame(GameInitialization message) throws RemoteException;
    void selectGame(GameSelection message) throws RemoteException;
    void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException;
    void insertInBookshelf(BookshelfInsertion message) throws RemoteException;
    void writeChat(ChatMessage message) throws RemoteException;
}
