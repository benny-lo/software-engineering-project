package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class WaitingUpdate extends Message {
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
