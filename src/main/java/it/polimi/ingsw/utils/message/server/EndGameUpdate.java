package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class EndGameUpdate extends Message {
    private final String winner;

    /**
     * Constructor for the class.
     * @param winner - the winner of the game
     */
    public EndGameUpdate(String winner) {
        this.winner = winner;
    }
    /**
     * Getter for the winner of the game
     * @return - the winner of the game
     */
    public String getWinner() {
        return winner;
    }
}
