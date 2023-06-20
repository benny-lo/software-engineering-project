package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class WaitingUpdate extends Message {
    private final String nickname;
    private final int missing;

    private final boolean isConnected;

    /**
     * Constructor for the class
     * @param nickname - the nickname of the player
     * @param isConnected - if the player has just connected it's true, else if the player has disconnected it's false
     * @param missing - how many players are missing
     */
    public WaitingUpdate(String nickname, int missing, boolean isConnected) {
        this.nickname = nickname;
        this.missing = missing;
        this.isConnected = isConnected;
    }
    /**
     * Getter for the just connected message
     * @return - the name of the player that has connected
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for the missing
     * @return - the number of players missing for the game
     */
    public int getMissing() {
        return missing;
    }

    /**
     * Getter for the type of the player's action
     * @return a boolean, true iff the player has connected otherwise false iff the player has disconnected
     */
    public boolean isConnected() {
        return isConnected;
    }
}
