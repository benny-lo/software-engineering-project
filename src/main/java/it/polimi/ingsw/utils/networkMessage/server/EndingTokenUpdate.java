package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class EndingTokenUpdate extends NetworkMessage {
    private final String owner;

    public EndingTokenUpdate(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
}
