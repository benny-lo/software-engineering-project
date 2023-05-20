package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class GameSelection extends Message {
    private final int id;

    public GameSelection(int id) {
        super();
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
