package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class ScoresUpdate extends Message {
    private final Map<String, Integer> scores;

    public ScoresUpdate(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}
