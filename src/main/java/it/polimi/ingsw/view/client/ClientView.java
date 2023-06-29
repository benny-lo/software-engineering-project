package it.polimi.ingsw.view.client;

import it.polimi.ingsw.network.client.ClientConnection;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMI;
import it.polimi.ingsw.network.client.socket.ClientConnectionTCP;
import it.polimi.ingsw.network.server.rmi.ConnectionEstablishmentRMIInterface;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.view.InputViewInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract class representing the client view.
 */
public abstract class ClientView implements ClientUpdateViewInterface, InputViewInterface {

    /**
     * The nickname of the client.
     */
    protected String nickname;

    /**
     * The network connection of the client to the server.
     */
    protected ClientConnection clientConnection;

    /**
     * Starts the RMI connection.
     * @param hostName The server hostname.
     * @param rmiPort The RMI port used by the server.
     */
    public void startRMI(String hostName, int rmiPort) {
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(hostName, rmiPort);
        } catch (RemoteException e) {
            Logger.clientError("registry not found");
            System.exit(0);
        }

        ConnectionEstablishmentRMIInterface stub = null;
        try {
            stub = (ConnectionEstablishmentRMIInterface) registry.lookup("ConnectionEstablishmentRMIInterface");
        } catch (AccessException e) {
            Logger.clientError("wrong access privileges for RMI");
            System.exit(0);
        } catch (NotBoundException e) {
            Logger.clientError("name not found for RMI");
            System.exit(0);
        } catch (ConnectException e) {
            Logger.clientError("server has not been started yet");
            System.exit(0);
        } catch (RemoteException e) {
            Logger.clientError("something went wrong with RMI");
            System.exit(0);
        }

        ClientConnectionRMI clientConnectionRMI = null;
        try {
            clientConnectionRMI = new ClientConnectionRMI(this);
        } catch (RemoteException e) {
            Logger.clientError("failed to instantiate RMI client");
            System.exit(0);
        }

        ServerConnectionRMIInterface serverConnection = null;
        try {
            serverConnection = stub.init(clientConnectionRMI);
        } catch (RemoteException e) {
            Logger.clientError("failed to get RMI server");
            e.printStackTrace();
            System.exit(0);
        }

        clientConnectionRMI.setServerConnectionRMIInterface(serverConnection);
        this.clientConnection = clientConnectionRMI;
        clientConnectionRMI.scheduleTimers();
    }

    /**
     * Starts the TCP connection.
     * @param hostName The server hostname.
     * @param socketPort The client socket port.
     */
    public void startTCP(String hostName, int socketPort) {
        Socket socket = null;
        try {
            socket = new Socket(hostName, socketPort);
        } catch (IOException e) {
            Logger.clientError("server has not been started yet");
            System.exit(0);
        }

        ClientConnectionTCP sender = null;
        try {
            sender = new ClientConnectionTCP(socket, this);
        } catch (IOException e) {
            Logger.clientError("failed to get socket streams from server");
            System.exit(0);
        }
        this.clientConnection = sender;
        (new Thread(sender)).start();
    }

    /**
     * Starts the GUI/CLI.
     */
    public abstract void start();

    /**
     * Checks if the nickname given is valid.
     * @param nickname The nickname that needs to be checked.
     * @return True if the nickname is valid, false if not.
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

    /**
     * {@inheritDoc}
     * @param message Message containing the chosen nickname.
     */
    @Override
    public void login(Nickname message) {
        clientConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message Message containing the information about the game to create.
     */
    @Override
    public void createGame(GameInitialization message) {
        clientConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message Message containing the id of the game chosen.
     */
    @Override
    public void selectGame(GameSelection message) {
        clientConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message Message containing the chosen positions.
     */
    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        clientConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message Message containing the column and the order in which to insert the chosen tiles.
     */
    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        clientConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message Message containing the text written.
     */
    @Override
    public void writeChat(ChatMessage message) {
        clientConnection.send(message);
    }
}
