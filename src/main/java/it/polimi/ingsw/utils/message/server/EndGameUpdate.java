package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class EndGameUpdate extends Message {
    private final String winner;

    public EndGameUpdate(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
