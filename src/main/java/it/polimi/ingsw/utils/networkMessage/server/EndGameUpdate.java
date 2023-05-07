package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class EndGameUpdate extends NetworkMessage {
    private final String winner;

    public EndGameUpdate(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
