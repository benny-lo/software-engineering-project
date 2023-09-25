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

import static java.lang.Integer.parseInt;

/**
 * Class starting the server.
 */
public class Server {
    private static final ConnectionEstablishmentRMI remoteObject = new ConnectionEstablishmentRMI();
    private static final int DEFAULT_RMI_PORT = 1099;
    private static final int DEFAULT_SOCKET_PORT = 1234;

    private Server() {}

    /**
     * Starts both the rmi connection and tcp connection.
     * @param args Command Line arguments hostname, rmi port and tcp port.
     */
    public static void launch(List<String> args) {
        try {
            if (args.size() == 3 && parseInt(args.get(1)) >= 0 && parseInt(args.get(1)) <= 65535 && parseInt(args.get(2)) >= 0 && parseInt(args.get(2)) <= 65535) {
                int rmiPort = Integer.parseInt(args.get(1));
                int tcpPort = Integer.parseInt(args.get(2));

                startConnectionRMI(args.get(0), rmiPort);
                startConnectionTCP(tcpPort);
            } else if (args.isEmpty()) {
                startConnectionRMI(DEFAULT_RMI_PORT);
                startConnectionTCP(DEFAULT_SOCKET_PORT);
            } else {
                Logger.serverError("wrong arguments : server [{hostName} {rmiPort} {socketPort}]");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            Logger.serverError("wrong arguments : server [{hostName} {rmiPort} {socketPort}]");
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
                while (true) startServer(server);
            } catch (IOException e) {
                Logger.serverError("failed to start server socket");
                System.exit(0);
            }
        })).start();
    }

    private static void startServer(ServerSocket server) {
        try {
            Socket socket = server.accept();
            ServerConnectionTCP serverConnectionTCP = new ServerConnectionTCP(socket);
            VirtualView view = new VirtualView(serverConnectionTCP);
            serverConnectionTCP.setServerInputViewInterface(view);
            serverConnectionTCP.start();
        } catch (IOException e) {
            Logger.serverError("server closed");
            System.exit(0);
        }
    }
}
