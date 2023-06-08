package it.polimi.ingsw.utils.game;

/**
 * Class representing a scoring/ending token. It is characterized by its score and its type.
 */
public class ScoringToken {
    /**
     * number of points given by {@code this}.
     */
    private final int score;

    /**
     * type of {@code this}.
     */
    private final int type;

    /**
     * Constructor of the class.
     * @param score score given by {@code this}.
     * @param type type of {@code this}.
     */
    public ScoringToken(int score, int type) {
        this.score = score;
        this.type = type;
    }

    /**
     * Getter of the score of {@code this}.
     * @return the score of {@code this}.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Getter of the type of {@code this}.
     * @return the type of {@code this}.
     */
    public int getType() {
        return this.type;
    }
}
