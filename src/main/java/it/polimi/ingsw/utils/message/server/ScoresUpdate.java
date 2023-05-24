package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class ScoresUpdate extends Message {
    private final Map<String, Integer> scores;

    /**
     * Constructor for the class
     * @param scores - the new scores of the players
     */
    public ScoresUpdate(Map<String, Integer> scores) {
        this.scores = scores;
    }
    /**
     * Getter for the scores
     * @return - a map of the new scores of the players.
     */
    public Map<String, Integer> getScores() {
        return scores;
    }
}
