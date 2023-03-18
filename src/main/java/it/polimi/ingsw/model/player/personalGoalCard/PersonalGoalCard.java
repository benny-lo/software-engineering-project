package it.polimi.ingsw.model.player.personalGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing a Personal Goal Card of a player
 */
public class PersonalGoalCard {
    /**
     * Interface representing a general grid pattern
     */
    private final PersonalGoalPatternInterface pattern;

    /**
     * Construct of this
     * @param pattern interface representing the pattern used by this
     */
    public PersonalGoalCard(PersonalGoalPatternInterface pattern) {
        this.pattern = pattern;
    }

    /**
     * Compute the personal score achieved by the bookshelf
     * @param bookshelf Bookshelf object to compute the personal score of
     * @return the personal score of {@code bookshelf}
     */
    public int getPersonalScore(Bookshelf bookshelf) {
        int matchings = pattern.check(bookshelf);
        return matchingToScore(matchings);
    }

    /**
     * Translate matching into the corresponding personal score
     * @param matchings number of matchings
     * @return score corresponding to {@code matching}
     */
    private int matchingToScore(int matchings) {
        if (matchings == 1) return 1;
        else if (matchings == 2) return 2;
        else if (matchings == 3) return 4;
        else if (matchings == 4) return 6;
        else if (matchings == 5) return 9;
        else return 12;
    }
}
