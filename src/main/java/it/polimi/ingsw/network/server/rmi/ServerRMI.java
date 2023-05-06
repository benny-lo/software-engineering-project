package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Lobby;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.utils.networkMessage.server.GameInfo;
import it.polimi.ingsw.network.client.rmi.ClientRMIInterface;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    private final Lobby lobby;

    public ServerRMI(Lobby lobby) throws RemoteException {
        super();
        this.lobby = lobby;
        startServerRMI();
    }

    private void startServerRMI(){
        try {
            Registry registry = LocateRegistry.createRegistry(ServerSettings.getRmiPort());
            registry.bind("ServerRMI", this);
            System.out.println("ServerRMI is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }

    @Override
    public List<GameInfo> login(String nickname, ClientRMIInterface clientRMIInterface) throws RemoteException {
        return lobby.login(nickname, new UpdateSenderRMI(clientRMIInterface));
    }

    @Override
    public boolean selectGame(String nickname, int id) throws RemoteException {
        return lobby.selectGame(nickname, id);
    }

    @Override
    public boolean createGame(String nickname, int numberPlayers, int numberCommonGoals) throws RemoteException {
        return lobby.createGame(nickname, numberPlayers, numberCommonGoals);
    }

    @Override
    public List<Item> selectFromLivingRoom(String nickname, List<Position> positions) throws RemoteException {
        return lobby.selectFromLivingRoom(nickname, positions);
    }

    @Override
    public boolean putInBookshelf(String nickname, int column, List<Integer> permutation) throws RemoteException {
        return lobby.putInBookshelf(nickname, column, permutation);
    }

    @Override
    public boolean addMessage(String nickname, String text) throws RemoteException {
        return lobby.addMessage(nickname, text);
    }
}
