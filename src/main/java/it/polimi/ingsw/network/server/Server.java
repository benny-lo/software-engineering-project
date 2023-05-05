package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.server.rmi.ServerRMI;
import it.polimi.ingsw.network.server.socket.ServerSocket;

import java.rmi.RemoteException;

public class Server {
    private final Lobby lobby;
    private ServerRMI serverRMI;
    private ServerSocket serverSocket;

    public static void main(String[] args) throws RemoteException {
        Server server = new Server();

        server.startServerRMI(server.getLobby());
        server.startServerSocket(server.getLobby());
    }

    public Server() {
        this.lobby = new Lobby();
    }

    private void startServerRMI(Lobby lobby) throws RemoteException {
        serverRMI = new ServerRMI(lobby);
    }

    private void startServerSocket(Lobby lobby) throws RemoteException {
        serverSocket = new ServerSocket(lobby);
    }

    public Lobby getLobby() {
        return lobby;
    }
}
