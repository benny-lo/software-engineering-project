package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.server.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.ClientView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;

public class ClientRMI implements ClientRMIInterface, Serializable {
    private ClientView view;
    private ServerRMIInterface serverRMIInterface;

    public ClientRMI() {
        try{
            //TODO: interaction between this and ClientView
            Registry registry = LocateRegistry.getRegistry(ServerSettings.getHostName(), ServerSettings.getRmiPort());
            serverRMIInterface = (ServerRMIInterface) registry.lookup("ServerRMI");
            serverRMIInterface.login("nickname_here", this);
            System.out.println("ClientRMI is connected.");
        }
        catch (Exception e){
            System.err.println("ClientRMI error");
            e.printStackTrace();
        }
    }

    @Override
    public void sendWaiting(int missing) throws RemoteException {
        // TODO
    }

    @Override
    public void sendLivingRoom(Item[][] livingRoom) throws RemoteException {
        view.setLivingRoom(livingRoom);
    }

    @Override
    public void sendBookshelf(String nickname, Item[][] bookshelf) throws RemoteException {
        view.setBookshelf(nickname, bookshelf);
    }

    @Override
    public void sendEndingToken(String endingToken) throws RemoteException {
        view.setEndingToken(endingToken);
    }

    @Override
    public void sendPersonalGoalCard(int id) throws RemoteException {
        view.setPersonalGoalCard(id);
    }

    @Override
    public void sendCommonGoalCard(int id, int top) throws RemoteException {
        view.setCommonGoalCards(id, top);
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        view.setChatMessage(message);
    }

    @Override
    public void sendScores(Map<String, Integer> scores) throws RemoteException {
        view.setScores(scores);
    }

    @Override
    public void sendStartTurn() throws RemoteException {
        // TODO
    }

    @Override
    public void sendEndGame() throws RemoteException {
        view.setEndGame();
    }
}
