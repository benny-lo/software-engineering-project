package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.Map;

public class ScoresUpdate extends NetworkMessage {
    private final Map<String, Integer> scores;

    public ScoresUpdate(Map<String, Integer> scores) {
        this.scores = scores;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}
