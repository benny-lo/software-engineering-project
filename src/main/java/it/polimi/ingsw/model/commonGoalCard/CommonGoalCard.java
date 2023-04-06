package it.polimi.ingsw.model.commonGoalCard;

import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
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
     * Tokens that players can take from {@code this}.
     */
    private final Stack<ScoringToken> tokens;

    /**
     * Constructor of the class.
     * @param pattern interface representing the pattern used by {@code this}.
     */
    public CommonGoalCard(int id, int numPlayers, CommonGoalPatternInterface pattern) {
        this.pattern = pattern;
        this.tokens = new Stack<>();

        if (numPlayers >= 4) {
            for(int i = 1; i <= 4; i++) tokens.push(new ScoringToken(2*i, id));
        } else if (numPlayers == 3) {
            for(int i = 2; i <= 4; i++) tokens.push(new ScoringToken(2*i, id));
        } else if (numPlayers == 2) {
            for(int i = 1; i <= 2; i++) tokens.push(new ScoringToken(4*i, id));
        }
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
