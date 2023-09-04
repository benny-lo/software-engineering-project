package it.polimi.ingsw.model.player.personalgoalcard;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.Item;
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
     * Constructor for the class. It sets the pattern corresponding to the provided {@code Map}.
     * @param maskPositions {@code Map} to set.
     */
    public PersonalGoalPattern(Map<Position, Item> maskPositions) {
        this.maskPositions = maskPositions;
    }

    /**
     * No-args constructor used for json. It creates an empty pattern.
     */
    public PersonalGoalPattern(){
        maskPositions = new HashMap<>();
    }


    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return Number of matches found on {@code bookshelf} with the pattern.
     */
    @Override
    public int check(Bookshelf bookshelf) {
        int matches = 0;
        for(Position p: maskPositions.keySet()){
            if(bookshelf.tileAt(p) == maskPositions.get(p)) matches++;
        }
        return matches;
    }

    /**
     * Getter for the map of {@code Position} to {@code Item} representing the mask of the pattern.
     * @return The {@code Map} representing the mask of the pattern.
     */
    public Map<Position, Item> getMaskPositions() {
        return new HashMap<>(maskPositions);
    }
}
