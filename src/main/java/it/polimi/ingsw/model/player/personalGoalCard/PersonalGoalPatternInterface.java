package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;


/**
 * Interface representing a pattern for personal goal cards.
 */
public interface PersonalGoalPatternInterface {
    /**
     * Check pattern on a {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return Number of matchings between {@code bookshelf} and the pattern.
     */
    int check(Bookshelf bookshelf);
}
