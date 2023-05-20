package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class PersonalGoalCardUpdate extends Message {
    private final int id;

    public PersonalGoalCardUpdate(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
