package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class AcceptedAction extends Message {
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
