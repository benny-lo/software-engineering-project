package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class ChatAccepted extends Message {
    private final boolean accepted;

    public ChatAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }
}
