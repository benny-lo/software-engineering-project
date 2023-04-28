package it.polimi.ingsw.view.rep;

import java.util.HashMap;
import java.util.Map;

public class ScoreRep extends Rep {
    private final Map<String, Integer> scores;

    public ScoreRep() {
        this.scores = new HashMap<>();
    }

    public Map<String, Integer> getScores() {
        peek();
        return scores;
    }

    public void updateRep(String nickname, int score) {
        scores.put(nickname, score);
    }
}
