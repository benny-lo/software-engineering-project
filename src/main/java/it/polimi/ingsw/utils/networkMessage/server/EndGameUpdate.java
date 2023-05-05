package it.polimi.ingsw.utils.networkMessage.server;

public class EndGameUpdate {
    private final String winner;

    public EndGameUpdate(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
