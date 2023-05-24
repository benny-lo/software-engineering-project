package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class ChatAccepted extends Message {
    private final boolean accepted;

    /**
     * Constructor for the class.
     * @param accepted - accpeted is true if the chat has been accepted, false if not
     */
    public ChatAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    /**
     * Getter for the accepted value
     * @return - the value of accepted.
     */
    public boolean isAccepted() {
        return accepted;
    }
}
