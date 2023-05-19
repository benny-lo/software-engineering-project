package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class GameSelection extends NetworkMessage {
    private final int id;

    public GameSelection(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
