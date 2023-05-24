package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class PersonalGoalCardUpdate extends Message {
    private final int id;

    /**
     * Constructor for the class
     * @param id - id of the new personal goal card
     */
    public PersonalGoalCardUpdate(int id) {
        this.id = id;
    }
    /**
     * Getter for the id
     * @return - the id of the updated goal card
     */
    public int getId() {
        return id;
    }
}
