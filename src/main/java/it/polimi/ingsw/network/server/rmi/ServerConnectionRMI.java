package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Lobby;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.RemoteException;
import java.util.List;


public class ServerConnectionRMI implements ServerConnectionRMIInterface {
    private final Lobby lobby;

    public ServerConnectionRMI(Lobby lobby){
        this.lobby = lobby;
    }

    @Override
    public void login(String nickname, ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        lobby.login(nickname, new SenderRMI(clientConnectionRMIInterface));
    }

    @Override
    public void selectGame(String nickname, int id) throws RemoteException {
        lobby.selectGame(nickname, id);
    }

    @Override
    public void createGame(String nickname, int numberPlayers, int numberCommonGoals) throws RemoteException {
        lobby.createGame(nickname, numberPlayers, numberCommonGoals);
    }

    @Override
    public void selectFromLivingRoom(String nickname, List<Position> positions) throws RemoteException {
        lobby.selectFromLivingRoom(nickname, positions);
    }

    @Override
    public void putInBookshelf(String nickname, int column, List<Integer> permutation) throws RemoteException {
        lobby.putInBookshelf(nickname, column, permutation);
    }

    @Override
    public void addMessage(String nickname, String text) throws RemoteException {
        lobby.addMessage(nickname, text);
    }
}
