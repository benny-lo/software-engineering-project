package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server for the acceptance of an insertion in the {@code Bookshelf}.
 */
public class AcceptedInsertion extends Message {
    private final boolean accepted;

    /**
     * Constructor for the class. It sets accepted.
     * @param accepted The acceptances of the move.
     */
    public AcceptedInsertion(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Getter for the accepted boolean.
     * @return The value of accepted.
     */
    public boolean isAccepted() {
        return accepted;
    }
}
