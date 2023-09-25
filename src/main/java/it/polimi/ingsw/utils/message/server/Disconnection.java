package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Disconnection message: sent, when a client disconnects, to the other clients.
 */
public class Disconnection extends Message {
    private final String disconnectedPlayer;

    /**
     * Constructor for {@code Disconnection} message: it initializes the nickname of the
     * player that disconnected.
     * @param disconnectedPlayer The nickname of the player that disconnected.
     */
    public Disconnection(String disconnectedPlayer) {
        super();
        this.disconnectedPlayer = disconnectedPlayer;
    }

    /**
     * Getter for the player that disconnected.
     * @return The nickname of the player that disconnected.
     */
    public String getDisconnectedPlayer() {
        return disconnectedPlayer;
    }
}
