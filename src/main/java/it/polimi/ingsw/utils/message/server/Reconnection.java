package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Reconnection message sent when a player (previously disconnected) rejoins the game.
 */
public class Reconnection extends Message {
    private final String reconnectedPlayer;

    /**
     * Constructor for a {@code Reconnection} message: it initializes the nickname of player that got
     * reconnected.
     * @param reconnectedPlayer The nickname of the player that got reconnected.
     */
    public Reconnection(String reconnectedPlayer) {
        super();
        this.reconnectedPlayer = reconnectedPlayer;
    }

    /**
     * Getter for the reconnected player.
     * @return The reconnected player's nickname.
     */
    public String getReconnectedPlayer() {
        return reconnectedPlayer;
    }
}
