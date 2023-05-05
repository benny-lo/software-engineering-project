package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class PersonalGoalCardUpdate extends NetworkMessage {
    private final int id;

    public PersonalGoalCardUpdate(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
