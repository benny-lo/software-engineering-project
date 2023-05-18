package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.client.UpdateReceiver;
import it.polimi.ingsw.network.client.rmi.RequestSenderRMI;
import it.polimi.ingsw.network.client.socket.RequestSenderTCP;
import it.polimi.ingsw.network.server.rmi.ServerConnectionRMIInterface;

import java.io.IOException;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClientView implements UpdateReceiver {
    protected String nickname;
    protected String currentPlayer;
    protected String winner;
    protected int numberPlayers;
    protected int numberCommonGoalCards;
    protected Item[][] livingRoom;
    protected Map<String, Item[][]> bookshelves;
    protected String endingToken;
    protected int personalGoalCard;
    protected Map<Integer, Integer> commonGoalCards;
    protected Map<String, Integer> scores;
    protected boolean endGame;
    protected List<Message> chat;
    protected List<Item> itemsChosen;
    protected RequestSender sender;

    public ClientView() {
        livingRoom = new Item[9][9];
        bookshelves = new HashMap<>();
        commonGoalCards = new HashMap<>();
        scores = new HashMap<>();
        chat = new ArrayList<>();
    }

    public void startRMI() {
        // TODO: exceptions
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ServerSettings.getHostName(), ServerSettings.getRmiPort());
        } catch (RemoteException e) {
            System.err.println("RMI registry error");
            throw new RuntimeException(e);
        }

        ServerConnectionRMIInterface stub;
        try {
            stub = (ServerConnectionRMIInterface) registry.lookup("ServerConnectionRMIInterface");
        } catch (AccessException e) {
            System.err.println("wrong access privileges for RMI");
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            System.err.println("name not found for RMI");
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            System.err.println("something went wrong for RMI");
            throw new RuntimeException(e);
        }

        try {
            sender = new RequestSenderRMI(stub, this);
        } catch(RemoteException e) {
            System.err.println("rmi client-side failed");
            throw new RuntimeException(e);
        }
    }

    public void startTCP() {
        // TODO: exceptions
        Socket socket;
        try {
            socket = new Socket(ServerSettings.getHostName(), ServerSettings.getSocketPort());
        } catch (IOException e) {
            System.err.println("not able to open TCP connection to Server");
            throw new RuntimeException(e);
        }

        RequestSenderTCP sender = null;
        try {
            sender = new RequestSenderTCP(socket, this);
        } catch (IOException e) {
            System.out.println("failed to get streams from Socket");
        }
        this.sender = sender;
        (new Thread(sender)).start();
    }

    public abstract void start();
}
