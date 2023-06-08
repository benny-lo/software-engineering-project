package it.polimi.ingsw.model.playerTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests of {@code Bookshelf}.
 */
public class BookshelfTest {
    /**
     * Test {@code Bookshelf} is constructed completely empty.
     */
    @Test
    public void testConstructionEmptyBookshelf() {
        Bookshelf b = new Bookshelf(2, 3);
        assertEquals(b.getRows(), 2);
        assertEquals(b.getColumns(), 3);

        for(int i = 0; i < b.getRows(); i++) {
            for(int j = 0; j < b.getColumns(); j++) {
                assertNull(b.tileAt(i, j));
            }
        }
    }

    /**
     * Test insertion of an {@code Item} in {@code Bookshelf}.
     */
    @Test
    public void testInsertOneItem() {
        Item item = Item.CAT;
        Bookshelf b = new Bookshelf();

        assertTrue(b.canInsert(1, 0));

        b.insert(item, 0);

        for(int i = 0; i < b.getRows(); i++) {
            for(int j = 0; j < b.getColumns(); j++) {
                if (i == 0 && j == 0) {
                    assertEquals(Item.CAT, b.tileAt(i, j));
                    continue;
                }
                assertNull(b.tileAt(i, j));
            }
        }
    }

    /**
     * Test insertion of two equal {@code Item}s in {@code Bookshelf}.
     */
    @Test
    public void testInsertTwoEqualItems() {
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(Item.CAT);
        items.add(Item.BOOK);

        assertTrue(b.canInsert(items.size(), 0));

        b.insert(items, 0);

        for(int i = 0; i < b.getRows(); i++) {
            for(int j = 0; j < b.getColumns(); j++) {
                if (i == 0 && j == 0) {
                    assertEquals(Item.CAT, b.tileAt(i, j));
                    continue;
                }
                if (i == 1 && j == 0) {
                    assertEquals(Item.BOOK, b.tileAt(i, j));
                    continue;
                }
                assertNull(b.tileAt(i, j));
            }
        }
    }

    /**
     * Test two consecutive insertions in the same column.
     */
    @Test
    public void testStackedInserts() {
        Bookshelf b = new Bookshelf();
        Item item1 = Item.CAT;
        Item item2 = Item.BOOK;

        b.canInsert(1, 0);
        b.insert(item1, 0);
        assertEquals(item1, b.tileAt(0, 0));

        b.canInsert(1, 0);
        b.insert(item2, 0);

        for(int i = 0; i < b.getRows(); i++) {
            for(int j = 0; j < b.getColumns(); j++) {
                if (i == 0 && j == 0) {
                    assertEquals(item1, b.tileAt(i, j));
                    continue;
                }
                if (i == 1 && j == 0) {
                    assertEquals(item2, b.tileAt(i, j));
                    continue;
                }
                assertNull(b.tileAt(i, j));
            }
        }
    }

    /**
     * Test filling the entire {@code bookshelf}.
     */
    @Test
    public void testFill() {
        Bookshelf b = new Bookshelf(3, 2);
        List<Item> firstColumn = new ArrayList<>(), secondColumn = new ArrayList<>();

        firstColumn.add(Item.CAT);
        firstColumn.add(Item.CAT);
        firstColumn.add(Item.CAT);

        secondColumn.add(Item.BOOK);
        secondColumn.add(Item.BOOK);
        secondColumn.add(Item.BOOK);

        assertTrue(b.canInsert(firstColumn.size(), 0));
        b.insert(firstColumn, 0);
        assertTrue(b.canInsert(secondColumn.size(), 1));
        b.insert(secondColumn, 1);

        assertTrue(b.isFull());
    }

    /**
     * Test score of an empty {@code Bookshelf}.
     */
    @Test
    public void testScoreEmpty() {
        Bookshelf b = new Bookshelf();
        assertEquals(0, b.getBookshelfScore());
    }

    /**
     * Test score of {@code Bookshelf} with a single item.
     */
    @Test
    public void testScoreOneItem() {
        Bookshelf b = new Bookshelf();

        assertTrue(b.canInsert(1, 2));

        b.insert(Item.CAT, 2);

        assertEquals(0, b.getBookshelfScore());
    }

    /**
     * Test score of full {@code Bookshelf} with all equal {@code Item}s.
     */
    @Test
    public void testScoreFullAllEqual() {
        Bookshelf b = new Bookshelf();
        for(int j = 0; j < b.getColumns(); j++) {
            for(int i = 0; i < b.getRows(); i++) {
                assertTrue(b.canInsert(1, j));
                b.insert(Item.CAT, j);
                assertEquals(Item.CAT, b.tileAt(i, j));
            }
        }

        assertEquals(8, b.getBookshelfScore());
    }

    /**
     * Test score on {@code Bookshelf} with distinct columns filled with the same {@code Item}.
     */
    @Test
    public void testScoreDistinctColumnsSameItem() {
        Bookshelf b = new Bookshelf(3, 3);
        Item item;
        for(int j = 0; j < b.getColumns(); j++) {
            if (j == 0) item = Item.CAT;
            else if (j == 1) item = Item.BOOK;
            else item = Item.CUP;

            for(int i = 0; i < b.getRows(); i++) {
                assertTrue(b.canInsert(1, j));
                b.insert(item, j);
                assertEquals(item, b.tileAt(i, j));
            }
        }

        assertEquals(6, b.getBookshelfScore());
    }
}
