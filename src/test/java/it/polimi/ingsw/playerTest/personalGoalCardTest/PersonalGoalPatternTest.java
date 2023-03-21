package it.polimi.ingsw.playerTest.personalGoalCardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Unit test of {@code PersonalGoalPattern}.
 */
public class PersonalGoalPatternTest {
    /**
     * Test {@code PersonalGoalPattern} constructor.
     */

    @Test
    public void testPersonalGoalPatternConstructor(){
        Position position = new Position(0, 0);
        Item item = Item.CAT;
        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position,item)));

        assertNotNull(pattern);
    }

    /**
     * Test {@code PersonaGoalPattern} inserts an {@code Item} in the {@code Bookshelf} and it checks if present in the pattern.
     */
    @Test
    public void testCheckOneItem(){
        Bookshelf bookshelf = new Bookshelf(6,5);
        Item item = Item.CAT;
        Map<Position, Item> mask = new HashMap<>();
        mask.put(new Position(0, 0), item);
        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);

        bookshelf.insert(item, 0);

        assertEquals(1, pattern.check(bookshelf));
    }

    /**
     * Test {@code PersonalGoalPattern} inserts two {@code Item} in the {@code Bookshelf} and it checks if present in the pattern.
     */
    @Test
    public void testCheckTwoItems(){
        Bookshelf bookshelf = new Bookshelf(6,5);
        Item item = Item.CAT;
        Item item1 = Item.BOOK;
        Map<Position, Item> mask = new HashMap<>();
        mask.put(new Position (0, 0), item);
        mask.put(new Position(2, 1), item1);
        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);

        bookshelf.insert(item, 0);
        bookshelf.insert(item, 1);
        bookshelf.insert(item, 1);
        bookshelf.insert(item1, 1);

        assertEquals(2, pattern.check(bookshelf));
    }

    /**
     * Test {@code PersonalGoalPattern} with an empty pattern.
     */
    @Test
    public void testEmptyPattern(){
        Bookshelf bookshelf = new Bookshelf(6,5);
        Item item = Item.CAT;
        Map<Position, Item> mask = new HashMap<>();
        PersonalGoalPattern pattern = new PersonalGoalPattern(mask);

        bookshelf.insert(item, 0);

        assertEquals(0, pattern.check(bookshelf));
    }
}