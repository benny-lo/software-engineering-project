package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server when the {@code Game} finishes or is aborted.
 */
public class EndGameUpdate extends Message {
    private final String winner;

    /**
     * Constructor for the class. It sets the winner, {@code null} to represent an error.
     * @param winner The winner of the game.
     */
    public EndGameUpdate(String winner) {
        this.winner = winner;
    }

    /**
     * Getter for the winner of the game.
     * @return The winner of the game
     */
    public String getWinner() {
        return winner;
    }
}
