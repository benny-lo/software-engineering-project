package it.polimi.ingsw.model.boardtest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.utils.Item;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@code Bag}.
 */
public class BagTest {
    /**
     * Test a new non-empty {@code Bag} is non-empty.
     */
    @Test
    public void testNonEmptyBag() {
        Bag bag = new Bag(1);
        assertFalse(bag.isEmpty());
    }

    /**
     * Test a new empty {@code Bag} is empty.
     */
    @Test
    public void testEmptyBag() {
        Bag bag = new Bag(0);
        assertTrue(bag.isEmpty());
    }

    /**
     * Test extract method on a non-empty {@code Bag} returns a non-null {@code Item}.
     */
    @Test
    public void testExtractNonEmptyBag() {
        Bag bag = new Bag(1);
        Item item = bag.extract();

        assertNotNull(item);
    }

    /**
     * Test extract method on an empty {@code Bag} returns null.
     */
    @Test
    public void testExtractEmptyBag() {
        Bag bag = new Bag(0);
        Item item = bag.extract();

        assertNull(item);
    }

    /**
     * Test a {@code Bag} that becomes empty after all {@code Item}s have been extracted.
     */
    @Test
    public void testBagIsEmptiedByExtract() {
        Bag bag = new Bag(1);
        Item item;
        for(int i = 0; i < 4; i++) {
            item = bag.extract();
            assertNotNull(item);
        }

        item = bag.extract();
        assertNotNull(item);
    }
}
