package it.polimi.ingsw.view.client;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMI;
import it.polimi.ingsw.network.client.socket.ClientConnectionTCP;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.view.InputViewInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ClientView implements ClientUpdateViewInterface, InputViewInterface {
    protected String nickname;
    protected ClientConnection clientConnection;

    /**
     * This method starts the RMI type of connection, catching different exceptions.
     * @param hostName - server IP
     * @param rmiPort - RMI port
     */
    public void startRMI(String hostName, int rmiPort) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(hostName, rmiPort);
        } catch (RemoteException e) {
            System.out.println("Registry not found.");
            System.exit(0);
        }
        System.out.println("got the registry");

        ConnectionEstablishmentRMIInterface stub = null;
        try {
            stub = (ConnectionEstablishmentRMIInterface) registry.lookup("ConnectionEstablishmentRMIInterface");
        } catch (AccessException e) {
            System.out.println("Wrong access privileges for RMI.");
            System.exit(0);
        } catch (NotBoundException e) {
            System.out.println("Name not found for RMI.");
            System.exit(0);
        } catch (RemoteException e) {
            System.out.println("Something went wrong with RMI.");
            System.exit(0);
        }
        System.out.println("get the ConnectionEstablishmentRMIInterface stub");

        ClientConnectionRMI clientConnectionRMI = null;
        try {
            clientConnectionRMI = new ClientConnectionRMI(this);
        } catch (RemoteException e) {
            System.out.println("Failed to instantiate RMI client.");
            System.exit(0);
        }

        System.out.println("created client connection RMI object");

        ServerConnectionRMIInterface serverConnection = null;
        try {
            serverConnection = stub.init(clientConnectionRMI);
        } catch (RemoteException e) {
            System.out.println("Failed to get RMI server.");
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("got the connection rmi");

        clientConnectionRMI.setServerConnectionRMIInterface(serverConnection);
        this.clientConnection = clientConnectionRMI;
        clientConnectionRMI.scheduleTimers();
    }

    /**
     * This method starts the TCP type of connection, catch different kinds of exceptions
     * @param hostName - server IP
     * @param socketPort - client socket port
     */
    public void startTCP(String hostName, int socketPort) {
        Socket socket = null;
        try {
            socket = new Socket(hostName, socketPort);
        } catch (IOException e) {
            System.out.println("Server has not been started yet.");
            System.exit(0);
        }

        ClientConnectionTCP sender = null;
        try {
            sender = new ClientConnectionTCP(socket, this);
        } catch (IOException e) {
            System.out.println("Failed to get socket streams from server.");
            System.exit(0);
        }
        this.clientConnection = sender;
        (new Thread(sender)).start();
    }

    public abstract void start();

    /**
     * This method checks if the nickname given is valid
     * @param nickname - the nickname that needs to be checked.
     * @return - true if the nickname is valid, false if not.
     */
    public static boolean isNicknameValid(String nickname){
        String regex = "^[A-Za-z]\\w{0,29}$";
        Pattern pattern = Pattern.compile(regex);

        if (nickname == null) {
            return true;
        }

        Matcher matcher = pattern.matcher(nickname);

        return !matcher.matches();
    }

    @Override
    public void login(Nickname message) {
        clientConnection.send(message);
    }

    @Override
    public void createGame(GameInitialization message) {
        clientConnection.send(message);
    }

    @Override
    public void selectGame(GameSelection message) {
        clientConnection.send(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        clientConnection.send(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        clientConnection.send(message);
    }

    @Override
    public void writeChat(ChatMessage message) {
        clientConnection.send(message);
    }
}
