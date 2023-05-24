package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class EndingTokenUpdate extends Message {
    private final String owner;

    /**
     * Constructor for the class.
     * @param owner - the owner of the endingToken
     */
    public EndingTokenUpdate(String owner) {
        this.owner = owner;
    }
    /**
     * Getter for the owner of the EndingToken
     * @return - the owner of the EndingToken
     */
    public String getOwner() {
        return owner;
    }
}
