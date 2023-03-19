package it.polimi.ingsw.board.commonGoalCardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern1;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for common goal cards having pattern 1
 */
public class CommonGoalCardPattern1Test {
    /**
     * Test of the pattern on an empty bookshelf.
     */
    @Test
    public void testOnEmptyBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(6, 5);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with the bottom row completely filled and the rest empty.
     */
    @Test
    public void testOnOneFilledRowBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(2, 3);

        for(int i = 0; i < bookshelf.getColumns(); i++) {
            bookshelf.insert(Item.CAT, i);
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with just a single 2x2 good square.
     */
    @Test
    public void testOnBookshelfOne2x2Square() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(3, 3);

        bookshelf.insert(Item.CAT, 0);
        bookshelf.insert(Item.CAT, 0);

        bookshelf.insert(Item.CAT, 1);
        bookshelf.insert(Item.CAT, 1);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with two horizontally overlapping 2x2 groups.
     */
    @Test
    public void testOnBookshelfTwoHorizontallyOverlappingGroups() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(3, 3);

        for(int j = 0; j < bookshelf.getColumns(); j++) {
            bookshelf.insert(Item.CAT, j);
            bookshelf.insert(Item.CAT, j);
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with two vertically overlapping 2x2 groups.
     */
    @Test
    public void testOnBookshelfTwoVerticallyOverlappingGroups() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(3, 5);

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 3; j++) {
                bookshelf.insert(Item.CAT, i);
            }
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with diagonally overlapping 2x2 groups.
     */
    @Test
    public void testOnBookshelfTwoDiagonallyOverlappingGroups() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(5, 3);
        bookshelf.insert(Item.BOOK, 0);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 2; j++) {
                bookshelf.insert(Item.CAT, i);
            }
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with two disjoint 2x2 groups of different kinds.
     */
    @Test
    public void testOnBookshelfTwoDisjointGroupsNotSameKind() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(3, 4);

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                bookshelf.insert(Item.CAT, i);
            }
        }

        for(int i = 2; i < 4; i++) {
            for(int j = 0; j < 2; j++) {
                bookshelf.insert(Item.BOOK, i);
            }
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with two disjoint 2x2 groups.
     */
    @Test
    public void testOnBookshelfTwoDisjointGroups() {
        CommonGoalPatternInterface pattern = new CommonGoalPattern1();
        Bookshelf bookshelf = new Bookshelf(3, 4);

        for(int j = 0; j < bookshelf.getColumns(); j++) {
            bookshelf.insert(Item.CAT, j);
            bookshelf.insert(Item.CAT, j);
        }

        assertTrue(pattern.check(bookshelf));
    }
}
