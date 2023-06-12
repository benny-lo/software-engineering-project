package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.client.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the rmi server connection on the client-side. It allows the client to
 * perform requests.
 */
public interface ServerConnectionRMIInterface extends Remote {
    /**
     * Performs the login of the client in the server, i.e. their nickname is set.
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void login(Nickname message) throws RemoteException;

    /**
     * Creates a new game according to the parameters specified by the client.
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void createGame(GameInitialization message) throws RemoteException;

    /**
     * Selects an already created game to join.
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void selectGame(GameSelection message) throws RemoteException;

    /**
     * Selects the tiles at the positions requested by the client.
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException;

    /**
     * Inserts the previously selected items (from the living room) into the client's bookshelf in
     * specified column and order.
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void insertInBookshelf(BookshelfInsertion message) throws RemoteException;

    /**
     * Sends a chat message (either broadcast or unicast).
     * @param message the request from the client.
     * @throws RemoteException rmi exception.
     */
    void writeChat(ChatMessage message) throws RemoteException;

    /**
     * Sends a beep to server (used as a heartbeat).
     * @param beep the beep from the client.
     * @throws RemoteException rmi exception.
     */
    void beep(Beep beep) throws RemoteException;
}
