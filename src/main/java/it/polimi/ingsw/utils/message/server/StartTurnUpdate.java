package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class StartTurnUpdate extends Message {
    private final String currentPlayer;

    public StartTurnUpdate(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }
}
