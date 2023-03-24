package it.polimi.ingsw.board.commonGoalCardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternCountGroups;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Unit tests for common goal card pattern 3.
 */
public class CommonGoalCardPattern3Test {
    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testOnEmptyBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf bookshelf = new Bookshelf(6, 6);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on full bookshelf with all items of the same kind.
     */
    @Test
    public void testOnFullBookshelfAllSame() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf bookshelf = new Bookshelf(4, 4);

        List<Item> items = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            items.add(Item.CAT);
        }

        for(int i = 0; i < 4; i++) {
            bookshelf.insert(items, i);
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test on full bookshelf with distinct items per column.
     */
    @Test
    public void testOnFullBookshelfDistinctColumns() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf bookshelf = new Bookshelf(4, 4);

        List<Item> items = new ArrayList<>();
        for(Item item : Stream.of(Item.values()).limit(4).toList()) {
            items.clear();
            for(int j = 0; j < 4; j++) {
                items.add(item);
            }
            bookshelf.insert(items, item.ordinal());
        }

        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test on almost full bookshelf: all columns filled except for one position.
     */
    @Test
    public void testOnFullBookshelfButOnePlace() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf bookshelf = new Bookshelf(4, 4);

        for(int i = 0; i < 3; i++) bookshelf.insert(Stream.of(Item.values()).limit(4).toList(), i);
        bookshelf.insert(Stream.of(Item.values()).limit(3).toList(), 3);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on bookshelf with two groups of columns: first and second are cats, third and fourth are books
     */
    @Test
    public void testOnBookshelfTwoGroupsColumns() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf  bookshelf = new Bookshelf(4, 4);
        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            items1.add(Item.CAT);
            items2.add(Item.BOOK);
        }

        bookshelf.insert(items1, 0); bookshelf.insert(items1, 1);
        bookshelf.insert(items2, 2); bookshelf.insert(items2, 3);

        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test on bookshelf with T-shaped cats horizontally and two separate groups of books.
     */
    @Test
    public void testOnBookshelfTShapedCatsAndTwoGroupsBooks() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,4, (s) -> s.size() == 4);
        Bookshelf bookshelf = new Bookshelf(5, 4);

        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();

        for(int i = 0; i < 5; i++) items1.add(Item.CAT);
        for(int i = 0; i < 2; i++) items2.add(Item.BOOK);
        items2.add(Item.CAT);
        for(int i = 0; i < 2; i++) items2.add(Item.CAT);

        bookshelf.insert(items1, 0);
        for(int i = 1; i < 4; i++) bookshelf.insert(items2, 1);

        assertFalse(pattern.check(bookshelf));
    }
}
