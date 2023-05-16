package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMI;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.network.server.socket.ClientHandler;
import it.polimi.ingsw.network.server.socket.ServerConnectionTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private final Lobby lobby;
    private ServerConnectionRMI serverConnectionRMI;
    private ServerConnectionTCP serverConnectionTCP;

    public static void main(String[] args) {
        Server server = new Server();

        server.startConnectionRMI(server.getLobby());
        server.startConnectionTCP(server.getLobby());

        System.out.println("server is ready ...");
    }

    public Server() {
        this.lobby = new Lobby();
    }

    private void startConnectionRMI(Lobby lobby) {
        ServerConnectionRMIInterface stub = null;
        try {
            serverConnectionRMI = new ServerConnectionRMI(lobby);
            stub = (ServerConnectionRMIInterface)
                    UnicastRemoteObject.exportObject(serverConnectionRMI, ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("failed to export serverRMI");
            e.printStackTrace();
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("failed to create registry");
            e.printStackTrace();
        }

        try {
            registry.bind("ServerConnectionRMIInterface", stub);
        } catch (AccessException e) {
            System.err.println("no permission to perform action");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.err.println("already bound to registry object with name ServerRMIInterface");
            e.printStackTrace();
        } catch (RemoteException e) {
            System.err.println("binding failed");
            e.printStackTrace();
        }
    }

    private void startConnectionTCP(Lobby lobby) {
        serverConnectionTCP = new ServerConnectionTCP(lobby);
        (new Thread(this::waitForClients)).start();
    }

    private void waitForClients() {
        ServerSocket server = null;

        try {
            server = new ServerSocket(ServerSettings.getSocketPort());
        } catch (IOException e) {
            System.err.println("failed to start server socket");
            e.printStackTrace();
        }

        while(true) {
            try {
                Socket socket = server.accept();
                (new Thread(new ClientHandler(socket, serverConnectionTCP))).start();
            } catch (IOException e) {
                System.err.println("server closed");
                e.printStackTrace();
            }
        }
    }

    public Lobby getLobby() {
        return lobby;
    }
}
