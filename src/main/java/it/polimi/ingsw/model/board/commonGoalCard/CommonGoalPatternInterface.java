package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Interface representing a pattern for common goal cards.
 */
public interface CommonGoalPatternInterface {
    /**
     * Check pattern on {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is on {@code bookshelf}.
     */
    boolean check(Bookshelf bookshelf);
}
