package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class AcceptedAction extends NetworkMessage {
    private final boolean accepted;

    private final AcceptedActionTypes type;

    public AcceptedAction(boolean accepted, AcceptedActionTypes type) {
        super();
        this.accepted = accepted;
        this.type = type;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public AcceptedActionTypes getType() {
        return type;
    }
}
