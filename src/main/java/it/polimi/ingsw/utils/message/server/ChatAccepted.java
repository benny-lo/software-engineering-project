package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server for the acceptance of a chat message.
 */
public class ChatAccepted extends Message {
    private final boolean accepted;

    /**
     * Constructor for the class. It sets the accepted field.
     * @param accepted The accepted boolean flag to set.
     */
    public ChatAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Getter for the accepted value.
     * @return The value of accepted.
     */
    public boolean isAccepted() {
        return accepted;
    }
}
