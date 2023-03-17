package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Item;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Bag
 */
public class BagTest {
    @Test
    public void testNonEmptyBag() {
        Bag bag = new Bag(1);
        assertFalse(bag.isEmpty());
    }

    @Test
    public void testEmptyBag() {
        Bag bag = new Bag(0);
        assertTrue(bag.isEmpty());
    }

    @Test
    public void testExtractNonEmptyBag() {
        Bag bag = new Bag(1);
        Item item = bag.extract();

        assertNotEquals(item, null);
    }

    @Test
    public void testExtractEmptyBag() {
        Bag bag = new Bag(0);
        Item item = bag.extract();

        assertNull(item);
    }

    @Test
    public void testBagIsEmptied() {
        Bag bag = new Bag(1);
        Item item;
        for(int i = 0; i < 4; i++) {
            item = bag.extract();
            assertNotNull(item);
        }

        item = bag.extract();
        assertNotEquals(item, null);
    }
}
