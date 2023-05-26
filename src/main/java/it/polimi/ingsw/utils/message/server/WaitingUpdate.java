package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class WaitingUpdate extends Message {
    private final String nickname;
    private final int missing;

    private final boolean typeOfAction;

    /**
     * Constructor for the class
     * @param nickname - the nickname of the player
     * @param typeOfAction - if the player has just connected it's true, else if the playes has disconnected it's false
     * @param missing - how many players are missing
     */
    public WaitingUpdate(String nickname, int missing, boolean typeOfAction) {
        this.nickname = nickname;
        this.missing = missing;
        this.typeOfAction = typeOfAction;
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
    public boolean isTypeOfAction() {
        return typeOfAction;
    }
}
