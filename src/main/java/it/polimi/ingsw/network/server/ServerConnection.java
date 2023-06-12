package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

/**
 * Interface used to represent a connection to (and from) a client on the server-side.
 */
public interface ServerConnection {
    /**
     * Sets the listener of the connection. It will be notified as new messages from the client.
     * @param serverInputViewInterface the listener.
     */
    void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface);

    /**
     * Send a {@code LivingRoomUpdate} to the client.
     * @param update the update to send.
     */
    void send(LivingRoomUpdate update);

    /**
     * Send a {@code BookshelfUpdate} to the client.
     * @param update the update to send.
     */
    void send(BookshelfUpdate update);

    /**
     * Send a {@code WaitingUpdate} to the client.
     * @param update the update to send.
     */
    void send(WaitingUpdate update);

    /**
     * Send a {@code ScoresUpdate} to the client.
     * @param update the update to send.
     */
    void send(ScoresUpdate update);

    /**
     * Send a {@code EndingTokenUpdate} to the client.
     * @param update the update to send.
     */
    void send(EndingTokenUpdate update);

    /**
     * Send a {@code CommonGoalCardsUpdate} to the client.
     * @param update the update to send.
     */
    void send(CommonGoalCardsUpdate update);

    /**
     * Send a {@code PersonalGoalCardUpdate} to the client.
     * @param update the update to send.
     */
    void send(PersonalGoalCardUpdate update);

    /**
     * Send a {@code ChatUpdate} to the client.
     * @param update the update to send.
     */
    void send(ChatUpdate update);

    /**
     * Send a {@code StartTurnUpdate} to the client.
     * @param update the update to send.
     */
    void send(StartTurnUpdate update);

    /**
     * Send a {@code EndGameUpdate} to the client.
     * @param update the update to send.
     */
    void send(EndGameUpdate update);

    /**
     * Send a {@code GamesList} to the client.
     * @param gamesList the message to send.
     */
    void send(GamesList gamesList);

    /**
     * Send a {@code SelectedItems} to the client.
     * @param selectedItems the message to send.
     */
    void send(SelectedItems selectedItems);

    /**
     * Send a {@code GameData} to the client.
     * @param gameData the message to send.
     */
    void send(GameData gameData);

    /**
     * Send a {@code AcceptedInsertion} to the client.
     * @param acceptedInsertion the message to send.
     */
    void send(AcceptedInsertion acceptedInsertion);

    /**
     * Send a {@code ChatAccepted} to the client.
     * @param chatAccepted the message to send.
     */
    void send(ChatAccepted chatAccepted);
}
