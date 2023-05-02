package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.chat.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ClientRMIInterface extends Remote {
    void sendWaiting(int missing) throws RemoteException;
    void sendLivingRoom(Item[][] livingRoom) throws RemoteException;
    void sendBookshelf(String nickname, Item[][] bookshelf) throws RemoteException;
    void sendEndingToken(String endingToken) throws RemoteException;
    void sendPersonalGoalCard(int id) throws RemoteException;
    void sendCommonGoalCard(int id, int top) throws RemoteException;
    void sendMessage(Message message) throws RemoteException;
    void sendScores(Map<String, Integer> scores) throws RemoteException;
    void sendStartTurn() throws RemoteException;
    void sendEndGame() throws RemoteException;
}
