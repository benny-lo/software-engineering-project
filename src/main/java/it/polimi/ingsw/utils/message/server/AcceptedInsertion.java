package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class AcceptedInsertion extends Message {
    private final boolean accepted;

    public AcceptedInsertion(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
