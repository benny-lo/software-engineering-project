package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class Disconnection extends Message {
    private final String disconnectedPlayer;

    public Disconnection(String disconnectedPlayer) {
        super();
        this.disconnectedPlayer = disconnectedPlayer;
    }

    public String getDisconnectedPlayer() {
        return disconnectedPlayer;
    }
}
