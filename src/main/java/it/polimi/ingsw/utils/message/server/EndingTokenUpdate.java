package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class EndingTokenUpdate extends Message {
    private final String owner;

    public EndingTokenUpdate(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}
