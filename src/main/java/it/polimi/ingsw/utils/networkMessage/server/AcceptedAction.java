package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class AcceptedAction extends NetworkMessage {
    private final boolean accepted;
    private final String description;

    public AcceptedAction(boolean accepted, String description) {
        super();
        this.accepted = accepted;
        this.description = description;
    }

    public boolean getAccepted() {
        return accepted;
    }
}
