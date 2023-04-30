package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.view.ClientView;

import java.rmi.RemoteException;
import java.util.Map;

public class ClientRMI implements ClientRMIInterface {
    private ClientView view;
    @Override
    public void sendLivingRoom(Item[][] livingRoom) throws RemoteException {
        view.setLivingRoom(livingRoom);
    }

    @Override
    public void sendBookshelf(String nickname, Item[][] bookshelf) throws RemoteException {
        view.setBookshelf(nickname, bookshelf);
    }

    @Override
    public void sendEndingToken() throws RemoteException {
        view.setEndingToken();
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
    public void sendEndGame() throws RemoteException {
        view.setEndGame();
    }
}
