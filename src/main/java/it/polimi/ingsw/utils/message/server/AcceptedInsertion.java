package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class AcceptedInsertion extends Message {
    private final boolean accepted;

    /**
     * Constructor for the class.
     * @param accepted - if the insertion has been accepted is true, if not it is false
     */
    public AcceptedInsertion(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Getter for the accepted boolean
     * @return - the value of accepted.
     */
    public boolean isAccepted() {
        return accepted;
    }
}
