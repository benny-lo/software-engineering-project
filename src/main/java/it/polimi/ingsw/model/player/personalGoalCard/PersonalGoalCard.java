package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing a Personal Goal Card of a player.
 */
public class PersonalGoalCard {
    /**
     * Interface representing a general bookshelf pattern.
     */
    private final PersonalGoalPatternInterface pattern;
    private final int id;

    /**
     * Construct of the class.
     * @param pattern interface representing the pattern used by {@code this}.
     */
    public PersonalGoalCard(int id, PersonalGoalPatternInterface pattern) {
        this.pattern = pattern;
        this.id = id;
    }

    /**
     * Compute the personal score achieved by the bookshelf.
     * @param bookshelf Bookshelf object to compute the personal score of.
     * @return the personal score of {@code bookshelf}.
     */
    public int getPersonalScore(Bookshelf bookshelf) {
        int matches = pattern.check(bookshelf);
        return matchingToScore(matches);
    }

    /**
     * Translate matching into the corresponding personal score.
     * @param matches number of matches.
     * @return score corresponding to {@code matching}.
     */
    private int matchingToScore(int matches) {
        if (matches == 0) return 0;
        else if (matches == 1) return 1;
        else if (matches == 2) return 2;
        else if (matches == 3) return 4;
        else if (matches == 4) return 6;
        else if (matches == 5) return 9;
        else if (matches == 6) return 12;
        else return -1;
    }

    /**
     * Getter of id for {@code this}
     * @return - returns id of the PersonalGoalCard
     */
    public int getId() {
        return id;
    }
}
