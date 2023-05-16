package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Lobby;

import it.polimi.ingsw.utils.networkMessage.server.GameInfo;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;


import java.rmi.RemoteException;
import java.util.List;


public class ServerConnectionRMI implements ServerConnectionRMIInterface {
    private final Lobby lobby;

    public ServerConnectionRMI(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public List<GameInfo> login(String nickname, ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        return lobby.login(nickname, new UpdateSenderRMI(clientConnectionRMIInterface));
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
