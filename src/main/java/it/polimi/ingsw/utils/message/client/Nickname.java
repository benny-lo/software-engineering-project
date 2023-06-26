package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the client representing a login.
 */
public class Nickname extends Message {
    private final String nickname;

    /**
     * Constructor for the class. It sets the nickname.
     * @param nickname The nickname of the player.
     */
    public Nickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    /**
     * Getter for the nickname.
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }
}
