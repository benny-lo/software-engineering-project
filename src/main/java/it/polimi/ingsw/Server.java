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
    private static Registry registry = null;
    private static ConnectionEstablishmentRMIInterface stub = null;
    /**
     * Parameters: [hostname] [RMI_port_number] [TCP_port_number]
     * @param args cli parameters (except for the first one which was server).
     */
    public static void launch(List<String> args) {
        try {
            if (args.size() == 3) {
                startConnectionRMI(args.get(0), Integer.parseInt(args.get(1)));
                startConnectionTCP(Integer.parseInt(args.get(2)));
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

    private static void startConnectionRMI(String hostname, int port) {
        System.setProperty("java.rmi.server.hostname", hostname);
        startConnectionRMI(port);
    }

    private static void startConnectionRMI(int port) {
        ConnectionEstablishmentRMI connection = new ConnectionEstablishmentRMI();
        try {
            stub = (ConnectionEstablishmentRMIInterface)
                    UnicastRemoteObject.exportObject(connection, port);
        } catch (RemoteException e) {
            System.out.println("Failed to export serverRMI.");
            System.exit(0);
        }

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
                        ServerConnectionTCP serverConnectionTCP;
                        try {
                            serverConnectionTCP = new ServerConnectionTCP(socket);
                        } catch (IOException e) {
                            continue;
                        }
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
