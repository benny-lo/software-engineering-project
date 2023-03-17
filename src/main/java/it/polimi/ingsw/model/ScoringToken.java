package it.polimi.ingsw.model;

public class ScoringToken {
    private final int score;
    private final int type;

    public ScoringToken(int score, int type) {
        this.score = score;
        this.type = type;
    }

    public int getScore() {
        return this.score;
    }

    public int getType() {
        return this.type;
    }
}
