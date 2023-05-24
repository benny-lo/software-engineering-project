package it.polimi.ingsw;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMI;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
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
import java.util.List;

public class Server {
    public static void launch(List<String> args) {
        try {
            if (args.size() == 2) {
                startConnectionRMI(Integer.parseInt(args.get(0)));
                startConnectionTCP(Integer.parseInt(args.get(1)));
            } else if (args.size() == 0) {
                startConnectionRMI(ServerSettings.getRmiPort());
                startConnectionTCP(ServerSettings.getSocketPort());
            } else {
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.exit(1);
        }


        System.out.println("server is ready ...");
    }

    private static void startConnectionRMI(int port) {
        ConnectionEstablishmentRMIInterface stub = null;
        ConnectionEstablishmentRMI connection = new ConnectionEstablishmentRMI();
        try {
            stub = (ConnectionEstablishmentRMIInterface)
                    UnicastRemoteObject.exportObject(connection, port);
        } catch (RemoteException e) {
            System.out.println("Failed to export serverRMI.");
            System.exit(0);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.out.println("Failed to create registry.");
            System.exit(0);
        }

        try {
            registry.bind("ConnectionEstablishmentRMIInterface", stub);
        } catch (AccessException e) {
            System.out.println("No permission to bind.");
            System.exit(0);
        } catch (AlreadyBoundException e) {
            System.err.println("Name ConnectionEstablishmentRMIInterface already bound to registry.");
            System.exit(0);
        } catch (RemoteException e) {
            System.err.println("Binding failed.");
            System.exit(0);
        }
    }

    private static void startConnectionTCP(int port) {
        (new Thread(() -> {
            try (ServerSocket server = new ServerSocket(port)) {
                while(true) {
                    try {
                        Socket socket = server.accept();
                        ServerConnectionTCP serverConnectionTCP = new ServerConnectionTCP(socket);
                        VirtualView view = new VirtualView(serverConnectionTCP);
                        serverConnectionTCP.setServerInputViewInterface(view);
                        Lobby.getInstance().addVirtualView(view);

                        (new Thread(serverConnectionTCP)).start();
                    } catch (IOException e) {
                        System.out.println("Server closed.");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to start server socket");
                System.exit(0);
            }
        })).start();
    }
}
