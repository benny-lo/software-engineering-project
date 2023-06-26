package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the client representing the selection of a game.
 */
public class GameSelection extends Message {
    private final int id;

    /**
     * Constructor for the class. It sets the id of a game.
     * @param id The id of the chosen game.
     */
    public GameSelection(int id) {
        super();
        this.id = id;
    }

    /**
     * Getter for the id.
     * @return The id of the chosen game.
     */
    public int getId() {
        return id;
    }
}
