package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server to players that are logged in a {@code Game} that has not yet started.
 */
public class WaitingUpdate extends Message {
    private final String nickname;
    private final int missing;
    private final boolean isConnected;

    /**
     * Constructor for the class. It sets the nickname of the player whose state changed and the number of players missing.
     * @param nickname The nickname of the player
     * @param isConnected {@code true} iff {@code nickname} connected.
     * @param missing The number of players still missing.
     */
    public WaitingUpdate(String nickname, int missing, boolean isConnected) {
        this.nickname = nickname;
        this.missing = missing;
        this.isConnected = isConnected;
    }

    /**
     * Getter for the nickname of the player whose state changed.
     * @return The nickname of the player whose state changed.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for the missing players.
     * @return The number of players missing for the game to start.
     */
    public int getMissing() {
        return missing;
    }

    /**
     * Getter for the type of action from the player with the stored nickname.
     * @return {@code true} iff the player is connected.
     */
    public boolean isConnected() {
        return isConnected;
    }
}
