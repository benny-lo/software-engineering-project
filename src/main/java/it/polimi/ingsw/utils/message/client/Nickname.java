package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class Nickname extends Message {
    private final String nickname;

    /**
     * Constructor for the class
     * @param nickname - nickname of the player
     */
    public Nickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    /**
     * Getter for the nickname
     * @return - the nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }
}
