package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.List;

public class GamesList extends NetworkMessage {
    private final List<GameInfo> available;

    public GamesList(List<GameInfo> available) {
        super();
        this.available = available;
    }

    public List<GameInfo> getAvailable() {
        return available;
    }
}
