package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Message sent by the server at the end of every turn and the end of the game.
 */
public class ScoresUpdate extends Message {
    private final Map<String, Integer> scores;

    /**
     * Constructor for the class. It sets the {@code Map} of updates scores.
     * @param scores The new scores of the players
     */
    public ScoresUpdate(Map<String, Integer> scores) {
        this.scores = scores;
    }

    /**
     * Getter for the {@code Map} of update scores.
     * @return {@code Map} of update scores.
     */
    public Map<String, Integer> getScores() {
        return new HashMap<>(scores);
    }
}
