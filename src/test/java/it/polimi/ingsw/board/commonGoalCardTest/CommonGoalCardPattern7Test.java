package it.polimi.ingsw.board.commonGoalCardTest;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;

/**
 * Unit test for common goal card pattern 7
 */
public class CommonGoalCardPattern7Test {
    /**
     * Test of pattern on an empty bookshelf
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(6,6);
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern with four different kinds in four rows
     */
    @Test
    public void fourKinds()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(5,6);
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.PLANT,3);
            bookshelf.insert(Item.CAT,4);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in four rows
     */
    @Test
    public void threeKinds()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(5,4);
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.FRAME,3);
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in three rows
     */
    @Test
    public void threeRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(5,4);
        for(int i=0;i<3;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.FRAME,3);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern with three different kinds in four rows not full
     */
    @Test
    public void notFull()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(4, true, 1, 3);
        Bookshelf bookshelf = new Bookshelf(5,4);
        for(int i=0;i<4;i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
        }
        assertFalse(pattern.check(bookshelf));
    }
}
