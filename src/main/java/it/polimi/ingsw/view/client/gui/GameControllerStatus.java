package it.polimi.ingsw.view.client.gui;

/**
 * Enumeration of the {@code GameController} statuses.
 */
public enum GameControllerStatus {
    /**
     * Status in which the player can only perform a selection on the living room and use the chat.
     */
    LIVING_ROOM,

    /**
     * Status in which the player can only perform an insertion in the bookshelf and use the chat.
     */
    BOOKSHELF,

    /**
     * Status in which the player waits for another player to make a move. The player can
     * still use the chat.
     */
    WAITING
}
