package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.Map;

/**
 * Interface representing a pattern for personal goal cards.
 */
public interface PersonalGoalPatternInterface {
    /**
     * Check pattern on a {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return number of matchings between {@code bookshelf} and the pattern.
     */
    int check(Bookshelf bookshelf);
    /**
     * Returns the maskPositions of the PersonalGoalPattern, overridden in implementation
     */
    public Map<Position, Item> getMaskPositions();
}
