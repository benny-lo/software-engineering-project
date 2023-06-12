package it.polimi.ingsw.utils.action;

/**
 * The listener for players' actions.
 */
public interface ActionListener {
    /**
     * Listens for a player to join into {@code this}.
     * @param action information passed to listener as a {@code JoinAction}
     */
    void perform(JoinAction action);

    /**
     * Listens for a player to select some tile from the living room.
     * @param action information passed to listener as a {@code SelectionFromLivingRoomAction}.
     */
    void perform(SelectionFromLivingRoomAction action);

    /**
     * Listens for a player to select the order and column where to insert the previously selected item tiles in their
     * bookshelf.
     * @param action information passed to listener as a {@code SelectionColumnAndOrderAction}.
     */
    void perform(SelectionColumnAndOrderAction action);

    /**
     * Listens for a player to send a chat message (either broadcast or unicast).
     * @param action information passed to listener as a {@code ChatMessageAction}.
     */
    void perform(ChatMessageAction action);

    /**
     * Listens for a player to disconnect.
     * @param action information passed to listener as a {@code DisconnectionAction}.
     */
    void perform(DisconnectionAction action);
}
