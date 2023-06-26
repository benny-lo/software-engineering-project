package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server when the {@code PersonalGoalCard} of a
 * player is selected.
 */
public class PersonalGoalCardUpdate extends Message {
    private final int id;

    /**
     * Constructor for the class. It sets the id of the {@code PersonalGoalCard}.
     * @param id The id of the {@code PersonalGoalCard}.
     */
    public PersonalGoalCardUpdate(int id) {
        this.id = id;
    }

    /**
     * Getter for the id.
     * @return The id of set {@code PersonalGoalCard}.
     */
    public int getId() {
        return id;
    }
}
