package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternCountGroups;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Unit tests for common goal card pattern 1.
 */
public class CommonGoalCardPattern1Test {
    static Predicate<Set<Position>> p = (s) -> {
        if (s.size() != 4) return false;
        List<Position> l = s.stream().sorted((p1, p2) -> (p1.getRow() != p2.getRow()) ? p1.getRow() - p2.getRow() : p1.getColumn() - p2.getColumn()).toList();
        Position firstPos = l.get(0);
        if (l.get(1).getRow() != firstPos.getRow() || l.get(1).getColumn() != firstPos.getColumn() + 1) return false;
        if (l.get(2).getRow() != firstPos.getRow() + 1 || l.get(2).getColumn() != firstPos.getColumn()) return false;
        return l.get(3).getRow() == firstPos.getRow() + 1 && l.get(3).getColumn() == firstPos.getColumn() + 1;
    };

    /**
     * Test of the pattern on an empty bookshelf.
     */
    @Test
    public void testOnEmptyBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with the bottom row completely filled and the rest empty.
     */
    @Test
    public void testOnOneFilledRowBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

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
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

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
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf(2, 2);

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
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

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
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();
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
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

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

        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on bookshelf with two disjoint 2x2 groups.
     */
    @Test
    public void testOnBookshelfTwoDisjointGroups() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(4,2, p);
        Bookshelf bookshelf = new Bookshelf();

        for(int j = 0; j < bookshelf.getColumns(); j++) {
            bookshelf.insert(Item.CAT, j);
            bookshelf.insert(Item.CAT, j);
        }

        assertTrue(pattern.check(bookshelf));
    }
}
