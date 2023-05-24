package it.polimi.ingsw.utils.action;

/**
 * The listener for players' actions.
 */
public interface ActionListener {
    /**
     * This method listens for a player to join
     * @param action - action that sets the listener in motion
     */
    void perform(JoinAction action);

    /**
     * This method listens for a player to make a selection from the living room
     * @param action - action that sets the listener in motion
     */
    void perform(SelectionFromLivingRoomAction action);

    /**
     * This method listens for a player to select column and order
     * @param action - action that sets the listener in motion
     */
    void perform(SelectionColumnAndOrderAction action);

    /**
     * This method listens for a Chat Message
     * @param action - action that sets the listener in motion
     */
    void perform(ChatMessageAction action);

    /**
     * This method listens for a Disconnection
     * @param action - action that sets the listener in motion
     */
    void perform(DisconnectionAction action);
}
