package it.polimi.ingsw.model.commongoalcardtest.pattern;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.utils.Item;

import static org.junit.jupiter.api.Assertions.*;


import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

/**
 * Unit test for common goal card pattern 5.
 */
public class CommonGoalCardPattern5Test {
    /**
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(3, false, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern with four different kinds in three columns.
     */
    @Test
    public void testFourKindsInThreeColumns()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(3, false, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<3;i++)
        {
            bookshelf.insert(Item.CUP,i);
            bookshelf.insert(Item.CAT,i);
            bookshelf.insert(Item.FRAME,i);
            bookshelf.insert(Item.PLANT,i);
            bookshelf.insert(Item.CAT,i);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in three columns.
     */
    @Test
    public void testThreeKindsInThreeColumns()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(3, false, 1, 3);
        Bookshelf bookshelf = new Bookshelf(3,3);
        for(int i=0;i<3;i++)
        {
            bookshelf.insert(Item.CUP,i);
            bookshelf.insert(Item.CAT,i);
            bookshelf.insert(Item.FRAME,i);
            bookshelf.insert(Item.CUP,i);
            bookshelf.insert(Item.CUP,i);
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in two columns.
     */
    @Test
    public void testThreeKindsInTwoColumns()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(3, false, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<2;i++)
        {
            bookshelf.insert(Item.CUP,i);
            bookshelf.insert(Item.CAT,i);
            bookshelf.insert(Item.PLANT,i);
            bookshelf.insert(Item.CUP,i);

        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern with three different kinds in a not full column.
     */
    @Test
    public void testThreeKindsNotFullColumn()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(3, false, 1, 3);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<3;i++)
        {
            bookshelf.insert(Item.CUP,i);
            bookshelf.insert(Item.CAT,i);
            bookshelf.insert(Item.PLANT,i);
            bookshelf.insert(Item.CUP,i);

        }
        assertFalse(pattern.check(bookshelf));
    }
}
