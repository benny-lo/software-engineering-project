package it.polimi.ingsw.model.commonGoalCard.commonGoalPattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Interface representing a pattern for common goal cards. (Strategy pattern).
 */
public interface CommonGoalPatternInterface {
    /**
     * Checks a pattern on {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    boolean check(Bookshelf bookshelf);
}
