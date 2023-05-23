package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.*;

/**
 * Implementation of PersonalGoalPatternInterface.
 */
public class PersonalGoalPattern implements PersonalGoalPatternInterface {
    /**
     * Map associating {@code Item}s with position in the Bookshelf.
     */
    private final Map<Position, Item> maskPositions;

    /**
     * Constructor of the class.
     * @param maskPositions map to assign to private attribute {@code maskPositions}.
     */
    public PersonalGoalPattern(Map<Position, Item> maskPositions) {
        this.maskPositions = maskPositions;
    }

    /**
     * Constructor of the class used for json.
     */
    public PersonalGoalPattern(){
        maskPositions = new HashMap<>();
    }


    /**
     * Check pattern on {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return - true if the pattern is found, false if it isn't
     */
    @Override
    public int check(Bookshelf bookshelf) {
        int matches = 0;
        for(Position p: maskPositions.keySet()){
            if(bookshelf.tileAt(p) == maskPositions.get(p)) matches++;
        }
        return matches;
    }
}
