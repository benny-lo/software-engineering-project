package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.server.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface representing the rmi client connection on the server-side. It allows the client to
 * perform requests.
 */
public interface ClientConnectionRMIInterface extends Remote {
    /**
     * Sends a {@code LivingRoomUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(LivingRoomUpdate update) throws RemoteException;

    /**
     * Sends a {@code BookshelfUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(BookshelfUpdate update) throws RemoteException;

    /**
     * Sends a {@code WaitingUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(WaitingUpdate update) throws RemoteException;

    /**
     * Sends a {@code ScoresUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(ScoresUpdate update) throws RemoteException;

    /**
     * Sends a {@code EndingTokenUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(EndingTokenUpdate update) throws RemoteException;

    /**
     * Sends a {@code CommonGoalCardsUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(CommonGoalCardsUpdate update) throws RemoteException;

    /**
     * Sends a {@code PersonalGoalCardUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(PersonalGoalCardUpdate update) throws RemoteException;

    /**
     * Sends a {@code ChatUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(ChatUpdate update) throws RemoteException;

    /**
     * Sends a {@code StartTurnUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(StartTurnUpdate update) throws RemoteException;

    /**
     * Sends a {@code EndGameUpdate} to the client.
     * @param update The update to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(EndGameUpdate update) throws RemoteException;

    /**
     * Sends a {@code GamesList} to the client.
     * @param list The {@code GamesList} to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(GamesList list) throws RemoteException;

    /**
     * Sends a {@code SelectedItems} to the client.
     * @param selected The {@code SelectedItems} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(SelectedItems selected) throws RemoteException;

    /**
     * Sends a {@code GameData} to the client.
     * @param gameData The {@code GameData} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(GameData gameData) throws RemoteException;

    /**
     * Sends a {@code AcceptedInsertion} to the client.
     * @param acceptedInsertion The {@code AcceptedInsertion} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(AcceptedInsertion acceptedInsertion) throws RemoteException;

    /**
     * Sends a {@code ChatAccepted} to the client.
     * @param chatAccepted The {@code ChatAccepted} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(ChatAccepted chatAccepted) throws RemoteException;

    void receive(Disconnection disconnection) throws RemoteException;

    void receive(Reconnection reconnection) throws RemoteException;

    /**
     * Sends a {@code Beep} to the client (used as a heartbeat).
     * @param beep The {@code Beep} object to send to the client.
     * @throws RemoteException Rmi exception.
     */
    void receive(Beep beep) throws RemoteException;
}
