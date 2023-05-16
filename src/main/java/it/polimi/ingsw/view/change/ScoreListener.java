package it.polimi.ingsw.view.change;

import java.util.HashMap;
import java.util.Map;

public class ScoreListener extends ModelListener {
    private final Map<String, Integer> scores;

    public ScoreListener() {
        this.scores = new HashMap<>();
    }

    public Map<String, Integer> getScores() {
        changed = false;
        Map<String, Integer> ret = new HashMap<>(scores);

        scores.clear();
        return ret;
    }

    public void updateState(String nickname, int score) {
        changed = true;
        scores.put(nickname, score);
    }
}
