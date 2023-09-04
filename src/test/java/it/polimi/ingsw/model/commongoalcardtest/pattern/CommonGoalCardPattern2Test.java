package it.polimi.ingsw.model.commongoalcardtest.pattern;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests for common goal card pattern 2.
 */
public class CommonGoalCardPattern2Test {
    /**
     * Test pattern on an empty bookshelf.
     */
    @Test
    public void testOnEmptyBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, false, 6, 6);
        Bookshelf bookshelf = new Bookshelf();
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on a bookshelf with only one filled column, with items of all different kinds.
     */
    @Test
    public void testOnFilledBookshelfAllKinds() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, false, 6, 6);
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>(List.of(Item.values()));

        items.remove(items.size() - 1);

        bookshelf.insert(items, 0);
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on bookshelf with two filled columns: one has all types, while the other has all type but one.
     */
    @Test
    public void testOnBookshelfFullTypeAndAllButOne() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, false, 6, 6);
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>(List.of(Item.values()));

        items.remove(items.size() - 1);

        bookshelf.insert(items, 0);
        items.remove(0);
        items.add(Item.CUP);
        bookshelf.insert(items, 1);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on bookshelf with two filled columns: both contain all types of items.
     */
    @Test
    public void testOnBookshelfFullTypeTwoColumns() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, false, 6, 6);
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>(List.of(Item.values()));

        items.remove(items.size() - 1);

        bookshelf.insert(items, 0);
        Collections.shuffle(items);
        bookshelf.insert(items, 1);

        assertTrue(pattern.check(bookshelf));
    }
}
