package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.List;

/**
 * Message sent by the server to tell the clients the selectable games.
 */
public class GamesList extends Message {
    private final List<GameInfo> available;

    /**
     * Constructor of the class. It sets the {@code List} of available games.
     * @param available {@code List} of {@code GameInfo}s.
     */
    public GamesList(List<GameInfo> available) {
        super();
        this.available = available;
    }

    /**
     * Getter for the available {@code List}.
     * @return The {@code List} of {@code GameInfo}s of the available games.
     */
    public List<GameInfo> getAvailable() {
        return available;
    }
}
