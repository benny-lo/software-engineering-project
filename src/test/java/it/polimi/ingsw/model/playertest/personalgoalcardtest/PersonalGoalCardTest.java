package it.polimi.ingsw.model.playertest.personalgoalcardtest;

import it.polimi.ingsw.model.player.personalgoalcard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalgoalcard.PersonalGoalPattern;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;

/**
 * Unit tests of {@code PersonalGoalCard}.
 */
public class PersonalGoalCardTest {

    /**
     * Test {@code PersonalGoalCard} is not null.
     */
    @Test
    public void testPersonalGoalCardConstructor(){
        Item item = Item.CAT;
        Map<Position, Item> mask = new HashMap<>();

        mask.put(new Position (0, 0), item);
        mask.put(new Position(2, 1), item);

        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(0, pattern);

        assertNotNull(personalGoalCard);
    }

    /**
     * Test {@code PersonalGoalCard} without a match.
     */
    @Test
    public void testGetPersonalScoreWithoutMatches(){
        Item item = Item.CAT;
        Item item1 = Item.BOOK;
        Bookshelf bookshelf = new Bookshelf();
        Map<Position, Item> mask = new HashMap<>();

        bookshelf.insert(item, 0);
        bookshelf.insert(List.of(item, item1), 1);

        mask.put(new Position (0, 0), item1);
        mask.put(new Position(1, 1), item);

        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(0, pattern);

        assertEquals(0, personalGoalCard.getPersonalScore(bookshelf));
    }

    /**
     * Test {@code PersonalGoalCard} with 2 matches.
     */
    @Test
    public void testGetPersonalScore2Matches(){
        Item item = Item.CAT;
        Item item1 = Item.BOOK;
        Bookshelf bookshelf = new Bookshelf();
        Map<Position, Item> mask = new HashMap<>();

        bookshelf.insert(item, 0);
        bookshelf.insert(List.of(item, item1), 1);

        mask.put(new Position (0, 0), item);
        mask.put(new Position(1, 1), item1);

        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(0, pattern);

        assertEquals(2, personalGoalCard.getPersonalScore(bookshelf));
    }

    /**
     * Test {@code PersonalGoalCard} with 7 matches, this case isn't achievable because a {@code PersonalGoalPattern} has at most 6 possible matches.
     */
    @Test
    public void testGetPersonalScore7Matches(){
        Item item = Item.CAT;
        Item item1 = Item.BOOK;
        Bookshelf bookshelf = new Bookshelf();
        Map<Position, Item> mask = new HashMap<>();

        bookshelf.insert(List.of(item, item1, item1), 0);
        bookshelf.insert(List.of(item1, item1, item1), 1);
        bookshelf.insert(List.of(item1, item1, item1), 2);


        mask.put(new Position (0, 0), item);
        mask.put(new Position(1, 0), item1);
        mask.put(new Position(2, 0), item1);
        mask.put(new Position(0, 1), item1);
        mask.put(new Position(1, 1), item1);
        mask.put(new Position(2, 1), item1);
        mask.put(new Position(1, 2), item1);


        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(0, pattern);

        assertEquals(-1, personalGoalCard.getPersonalScore(bookshelf));
    }
}