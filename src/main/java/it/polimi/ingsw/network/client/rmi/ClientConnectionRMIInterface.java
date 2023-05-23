package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.server.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientConnectionRMIInterface extends Remote {
    void receive(LivingRoomUpdate update) throws RemoteException;
    void receive(BookshelfUpdate update) throws RemoteException;
    void receive(WaitingUpdate update) throws RemoteException;
    void receive(ScoresUpdate update) throws RemoteException;
    void receive(EndingTokenUpdate update) throws RemoteException;
    void receive(CommonGoalCardsUpdate update) throws RemoteException;
    void receive(PersonalGoalCardUpdate update) throws RemoteException;
    void receive(ChatUpdate update) throws RemoteException;
    void receive(StartTurnUpdate update) throws RemoteException;
    void receive(EndGameUpdate update) throws RemoteException;
    void receive(GamesList list) throws RemoteException;
    void receive(ItemsSelected selected) throws RemoteException;
    void receive(GameData gameData) throws RemoteException;
    void receive(AcceptedInsertion acceptedInsertion) throws RemoteException;
    void receive(ChatAccepted chatAccepted) throws RemoteException;
    void receive(Beep beep) throws RemoteException;
}
