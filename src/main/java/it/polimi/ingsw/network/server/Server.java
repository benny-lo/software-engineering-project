package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.server.rmi.ServerRMI;
import it.polimi.ingsw.network.server.rmi.ServerRMIInterface;
import it.polimi.ingsw.network.server.socket.ClientHandler;
import it.polimi.ingsw.network.server.socket.ServerTCP;

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
    private ServerRMI serverRMI;
    private ServerTCP serverTCP;

    public static void main(String[] args) {
        Server server = new Server();

        server.startServerRMI(server.getLobby());
        server.startServerSocket(server.getLobby());

        System.out.println("server is read ...");
    }

    public Server() {
        this.lobby = new Lobby();
    }

    private void startServerRMI(Lobby lobby) {
        ServerRMIInterface stub = null;
        try {
            serverRMI = new ServerRMI(lobby);
            stub = (ServerRMIInterface)
                    UnicastRemoteObject.exportObject(serverRMI, ServerSettings.getRmiPort());
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
            registry.bind("ServerRMIInterface", stub);
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

    private void startServerSocket(Lobby lobby) {
        serverTCP = new ServerTCP(lobby);
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
                (new Thread(new ClientHandler(socket, serverTCP))).start();
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
