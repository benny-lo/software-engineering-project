package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMI;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.socket.ServerConnectionTCP;
import it.polimi.ingsw.network.server.socket.VirtualViewTCP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Server {
    public void launch(List<String> args) {
        Lobby lobby = new Lobby();

        startConnectionRMI(lobby);
        startConnectionTCP(lobby);

        System.out.println("server is ready ...");
    }

    private void startConnectionRMI(Lobby lobby) {
        ConnectionEstablishmentRMIInterface stub = null;
        ConnectionEstablishmentRMI connection = new ConnectionEstablishmentRMI(lobby);
        try {
            stub = (ConnectionEstablishmentRMIInterface)
                    UnicastRemoteObject.exportObject(connection, ServerSettings.getRmiPort());
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
            registry.bind("ConnectionEstablishmentRMIInterface", stub);
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
        (new Thread(() -> {
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
                    ServerConnectionTCP serverConnectionTCP = new ServerConnectionTCP(socket);
                    VirtualView view = new VirtualViewTCP(lobby, serverConnectionTCP);
                    serverConnectionTCP.setReceiver(view);
                    lobby.addVirtualView(view);

                    (new Thread(serverConnectionTCP)).start();
                } catch (IOException e) {
                    System.err.println("server closed");
                    e.printStackTrace();
                }
            }
        })).start();
    }
}
