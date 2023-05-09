package it.polimi.ingsw.model.playerTest.personalGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
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
        Bookshelf bookshelf = new Bookshelf(6,5);
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
        Bookshelf bookshelf = new Bookshelf(6,5);
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
     * Test {@code PersonalGoalCard} with 7 matches.
     */
    @Test
    public void testGetPersonalScore7Matches(){
        Item item = Item.CAT;
        Item item1 = Item.BOOK;
        Bookshelf bookshelf = new Bookshelf(3,3);
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