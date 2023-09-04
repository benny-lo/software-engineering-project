package it.polimi.ingsw.model.commongoalcardtest.pattern;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.Item;

/**
 * Unit test for common goal card pattern 6.
 */

public class CommonGoalCardPattern6Test {
    /**
     * Testing pattern on empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf();
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Testing pattern on two rows of 4 different kinds.
     */
    @Test
    public void testFourKindsOnTwoRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<2;i++)
        {
            for(int j=0;j<bookshelf.getColumns();j++)
            {
                bookshelf.insert(Item.CUP,j);
                bookshelf.insert(Item.CAT,j);
                bookshelf.insert(Item.FRAME,j);
                bookshelf.insert(Item.PLANT,j);
                bookshelf.insert(Item.CAT,j);
            }
        }

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Testing pattern on 1 row with 5 kinds.
     */
    @Test
    public void testFiveKindsOnOneRow()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.insert(Item.CUP,0);
        bookshelf.insert(Item.CAT,1);
        bookshelf.insert(Item.FRAME,2);
        bookshelf.insert(Item.PLANT,3);
        bookshelf.insert(Item.BOOK,4);

        assertFalse(pattern.check(bookshelf));

    }

    /**
     * Testing pattern on 3 rows with 5 kinds.
     */
    @Test
    public void testFiveKindsOnThreeRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0;i<bookshelf.getRows();i++)
        {
            bookshelf.insert(Item.CUP,0);
            bookshelf.insert(Item.CAT,1);
            bookshelf.insert(Item.FRAME,2);
            bookshelf.insert(Item.PLANT,3);
            bookshelf.insert(Item.BOOK,4);

        }
        assertTrue(pattern.check(bookshelf));
    }

}
