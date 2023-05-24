package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.List;

public class GamesList extends Message {
    private final List<GameInfo> available;

    /**
     * Constructor of the class
     * @param available - list of available games
     */
    public GamesList(List<GameInfo> available) {
        super();
        this.available = available;
    }
    /**
     * Getter for the available list
     * @return - the list of game infos of the available games
     */
    public List<GameInfo> getAvailable() {
        return available;
    }
}
