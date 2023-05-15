package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionRMIInterface extends Remote {
    void sendLivingRoomUpdate(LivingRoomUpdate update) throws RemoteException;
    void sendBookshelfUpdate(BookshelfUpdate update) throws RemoteException;
    void sendWaitingUpdate(WaitingUpdate update) throws RemoteException;
    void sendScoresUpdate(ScoresUpdate update) throws RemoteException;
    void sendEndingTokenUpdate(EndingTokenUpdate update) throws RemoteException;
    void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) throws RemoteException;
    void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) throws RemoteException;
    void sendChatUpdate(ChatUpdate update) throws RemoteException;
    void sendStartTurnUpdate(StartTurnUpdate update) throws RemoteException;
    void sendEndGameUpdate(EndGameUpdate update) throws RemoteException;
}
