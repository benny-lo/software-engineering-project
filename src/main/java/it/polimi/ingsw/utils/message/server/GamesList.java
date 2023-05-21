package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.List;

public class GamesList extends Message {
    private final List<GameInfo> available;

    public GamesList(List<GameInfo> available) {
        super();
        this.available = available;
    }

    public List<GameInfo> getAvailable() {
        return available;
    }
}
