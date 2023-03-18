package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.Stack;

/**
 * Class representing a Common Goal Card. It consists of a pattern and a stack of scoring tokens.
 */
public class CommonGoalCard {
    /**
     * Interface representing a general bookshelf pattern.
     */
    private final CommonGoalPatternInterface pattern;

    /**
     * tokens that players can take from {@code this}.
     */
    private final Stack<ScoringToken> tokens;

    /**
     * identifier of {@code this}.
     */
    private final int id;

    /**
     * Constructor of the class.
     * @param pattern interface representing the pattern used by {@code this}.
     * @param id identifier to associate to {@code this}.
     */
    public CommonGoalCard(CommonGoalPatternInterface pattern, int id) {
        this.pattern = pattern;
        this.tokens = new Stack<>();
        this.id = id;
    }

    /**
     * Put a token on the stack of scoring tokens of {@code this}.
     * @param score score of token to place on the stack.
     */
    public void pushToken(int score) {
        ScoringToken token = new ScoringToken(score, id);
        tokens.push(token);
    }

    /**
     * Take scoring token from stack of scoring tokens of {@code this}.
     * @return the {@code ScoringToken} taken.
     */
    public ScoringToken popToken() {
        return tokens.pop();
    }

    /**
     * Check if a {@code Bookshelf} object satisfies the pattern.
     * @param bookshelf the {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern of {@code this} is on {@code bookshelf}.
     */
    public boolean checkPattern(Bookshelf bookshelf) {
        return pattern.check(bookshelf);
    }
}
