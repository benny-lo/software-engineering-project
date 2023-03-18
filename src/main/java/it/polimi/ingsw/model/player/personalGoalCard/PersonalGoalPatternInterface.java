package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Interface representing a pattern for personal goal cards
 */
public interface PersonalGoalPatternInterface {
    /**
     * Check pattern on a Bookshelf object
     * @param bookshelf Bookshelf object to check the pattern on
     * @return number of matchings between {@code bookshelf} and the pattern
     */
    int check(Bookshelf bookshelf);
}
