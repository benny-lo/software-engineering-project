package it.polimi.ingsw;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.view.server.VirtualView;
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
    private static final ConnectionEstablishmentRMI remoteObject = new ConnectionEstablishmentRMI();
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final int DEFAULT_SOCKET_PORT = 1234;
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
                startConnectionRMI(DEFAULT_RMI_PORT);
                startConnectionTCP(DEFAULT_SOCKET_PORT);
            } else {
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.exit(1);
        }


        Logger.serverMessage("server is ready ...");
    }

    private static void startConnectionRMI(String hostname, int port) {
        System.setProperty("java.rmi.server.hostname", hostname);
        startConnectionRMI(port);
    }

    private static void startConnectionRMI(int port) {
        ConnectionEstablishmentRMIInterface stub = null;

        try {
            stub = (ConnectionEstablishmentRMIInterface)
                    UnicastRemoteObject.exportObject(remoteObject, port);
        } catch (RemoteException e) {
            Logger.serverError("failed to export serverRMI");
            System.exit(0);
        }

        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            Logger.serverError("failed to create registry");
            System.exit(0);
        }

        try {
            registry.bind("ConnectionEstablishmentRMIInterface", stub);
        } catch (AccessException e) {
            Logger.serverError("no permission to bind");
            System.exit(0);
        } catch (AlreadyBoundException e) {
            Logger.serverError("name ConnectionEstablishmentRMIInterface already bound to registry");
            System.exit(0);
        } catch (RemoteException e) {
            Logger.serverError("binding failed");
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
                        (new Thread(serverConnectionTCP)).start();
                    } catch (IOException e) {
                        Logger.serverError("server closed");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                Logger.serverError("failed to start server socket");
                System.exit(0);
            }
        })).start();
    }
}
