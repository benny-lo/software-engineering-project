package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class WaitingUpdate extends Message {
    private final String justConnected;
    private final int missing;

    /**
     * Constructor for the class
     * @param justConnected - if the player has just connected
     * @param missing - if the player is missing
     */
    public WaitingUpdate(String justConnected, int missing) {
        this.justConnected = justConnected;
        this.missing = missing;
    }
    /**
     * Getter for the just connected message
     * @return - the just connected string
     */
    public String getJustConnected() {
        return justConnected;
    }
    /**
     * Getter for the missing
     * @return - the missing value
     */
    public int getMissing() {
        return missing;
    }
}
