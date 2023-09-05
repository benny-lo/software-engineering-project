package it.polimi.ingsw.model.commongoalcard;

import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.*;

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
    private final Deque<ScoringToken> tokens;

    private final int id;

    /**
     * Constructor for the class. It sets {@code id}, {@code numPlayers} and {@code pattern}.
     * @param id int representing the ID of {@code this}.
     * @param numPlayers The number of players in the game (used to determine the stack of {@code ScoringToken}s).
     * @param pattern Interface representing the pattern used by {@code this}.
     */
    public CommonGoalCard(int id, int numPlayers, CommonGoalPatternInterface pattern) {
        this.pattern = pattern;
        this.tokens = new ArrayDeque<>();
        this.id = id;

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
        try {
            return tokens.pop();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Check if a {@code Bookshelf} object satisfies the pattern.
     * @param bookshelf The {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern of {@code this} is on {@code bookshelf}.
     */
    public boolean checkPattern(Bookshelf bookshelf) {
        return pattern.check(bookshelf);
    }

    /**
     * Getter of score of the tokens' stack
     * @return The score of the first token without removing it. Throw exception if the stack is Empty.
     */
    public int getTopStack() {
        return Optional.ofNullable(tokens.peek()).map(ScoringToken::getScore).orElse(0);
    }

    /**
     * Getter of id of {@code this}
     * @return id of the CommonGoalCard
     */
    public int getId() {
        return id;
    }
}
