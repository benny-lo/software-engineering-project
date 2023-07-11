package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class Reconnection extends Message {
    private final String reconnectedPlayer;

    public Reconnection(String reconnectedPlayer) {
        super();
        this.reconnectedPlayer = reconnectedPlayer;
    }

    public String getReconnectedPlayer() {
        return reconnectedPlayer;
    }
}
