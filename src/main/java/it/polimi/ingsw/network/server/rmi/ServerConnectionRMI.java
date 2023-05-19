package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.server.ServerReceiver;
import it.polimi.ingsw.utils.networkMessage.client.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerConnectionRMI extends UnicastRemoteObject implements ServerConnectionRMIInterface {
    private final ServerReceiver receiver;

    public ServerConnectionRMI(ServerReceiver receiver) throws RemoteException {
        super();
        this.receiver = receiver;
    }

    @Override
    public void login(Nickname message) throws RemoteException {
        receiver.login(message);
    }

    @Override
    public void createGame(GameInitialization message) throws RemoteException {
        receiver.createGame(message);
    }

    @Override
    public void selectGame(GameSelection message) throws RemoteException {
        receiver.selectGame(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException {
        receiver.selectFromLivingRoom(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) throws RemoteException {
        receiver.insertInBookshelf(message);
    }

    @Override
    public void writeChat(ChatMessage message) throws RemoteException {
        receiver.writeChat(message);
    }
}
