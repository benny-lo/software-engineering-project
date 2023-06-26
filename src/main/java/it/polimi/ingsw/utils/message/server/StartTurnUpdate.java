package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server when the turn of player begins.
 */
public class StartTurnUpdate extends Message {
    private final String currentPlayer;

    /**
     * Constructor for the class. Its sets the current player.
     * @param currentPlayer The player whose turn just started.
     */
    public StartTurnUpdate(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Getter for the new current player.
     * @return The new current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
