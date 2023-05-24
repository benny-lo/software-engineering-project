package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class GameSelection extends Message {
    private final int id;

    /**
     * Constructor for the class
     * @param id - id of the right Game
     */
    public GameSelection(int id) {
        super();
        this.id = id;
    }

    /**
     * Getter for the id
     * @return - the id.
     */
    public int getId() {
        return id;
    }
}
