package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class AcceptedAction extends NetworkMessage {
    private final boolean accepted;

    public AcceptedAction(boolean accepted) {
        super();
        this.accepted = accepted;
    }

    public boolean getAccepted() {
        return accepted;
    }
}
