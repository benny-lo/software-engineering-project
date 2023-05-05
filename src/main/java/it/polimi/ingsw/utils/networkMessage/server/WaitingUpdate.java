package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class WaitingUpdate extends NetworkMessage {
    private final String justConnected;
    private final int missing;

    public WaitingUpdate(String justConnected, int missing) {
        this.justConnected = justConnected;
        this.missing = missing;
    }

    public String getJustConnected() {
        return justConnected;
    }

    public int getMissing() {
        return missing;
    }
}
