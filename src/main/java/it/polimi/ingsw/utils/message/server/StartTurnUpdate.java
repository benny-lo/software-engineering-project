package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class StartTurnUpdate extends Message {
    private final String currentPlayer;

    /**
     * Constructor for the class.
     * @param currentPlayer - the new current player
     */
    public StartTurnUpdate(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    /**
     * Getter for the new current player
     * @return - the new current player
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
