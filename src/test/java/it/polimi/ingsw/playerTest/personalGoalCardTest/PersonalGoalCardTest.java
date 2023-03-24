package it.polimi.ingsw.playerTest.personalGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(pattern);

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
        bookshelf.insert(item, 0);
        bookshelf.insert(item, 1);
        bookshelf.insert(item1, 1);

        Map<Position, Item> mask = new HashMap<>();
        mask.put(new Position (0, 0), item1);
        mask.put(new Position(1, 1), item);
        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(pattern);

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
        bookshelf.insert(item, 0);
        bookshelf.insert(item, 1);
        bookshelf.insert(item1, 1);

        Map<Position, Item> mask = new HashMap<>();
        mask.put(new Position (0, 0), item);
        mask.put(new Position(1, 1), item1);
        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(pattern);

        assertEquals(2, personalGoalCard.getPersonalScore(bookshelf));
    }
}