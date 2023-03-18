package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.Map;

/**
 * Implementation of PersonalGoalPatternInterface
 */
public class PersonalGoalPattern implements PersonalGoalPatternInterface {
    /**
     * Map associating {@code Item}s with position in the Bookshelf
     */
    private final Map<Position, Item> maskPositions;

    /**
     * Constructor of the class
     * @param maskPositions map to assign to private attribute {@code maskPositions}
     */
    public PersonalGoalPattern(Map<Position, Item> maskPositions) {
        this.maskPositions = maskPositions;
    }

    public int check(Bookshelf bookshelf) {
        int matchings = 0;
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 5; j++) {
                Position p = new Position(i, j);
                if (maskPositions.get(p) == bookshelf.tileAt(p)) matchings++;
            }
        }
        return matchings;
    }
}
