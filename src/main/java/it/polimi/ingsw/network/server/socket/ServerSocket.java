package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.Lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerSocket extends UnicastRemoteObject implements ServerSocketInterface {
    private final Lobby lobby;

    public ServerSocket(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
    }
}
