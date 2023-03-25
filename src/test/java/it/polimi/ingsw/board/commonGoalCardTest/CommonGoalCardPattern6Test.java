package it.polimi.ingsw.board.commonGoalCardTest;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternDistinctItems;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;

/**
 * Unit test for common goal card pattern 6
 */

public class CommonGoalCardPattern6Test {
    /**
     * Testing pattern on empty bookshelf
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf(6,6);
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Testing pattern on two rows of 4 different kinds
     */
    @Test
    public void fourKinds()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf(5,5);
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
     * Testing pattern on 1 row with 5 kinds
     */
    @Test
    public void oneRow()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf(5,5);
        bookshelf.insert(Item.CUP,0);
        bookshelf.insert(Item.CAT,1);
        bookshelf.insert(Item.FRAME,2);
        bookshelf.insert(Item.PLANT,3);
        bookshelf.insert(Item.BOOK,4);

        assertFalse(pattern.check(bookshelf));

    }

    /**
     * Testing pattern on 3 row with 5 kinds
     */
    @Test
    public void threeRows()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternDistinctItems(2, true, 5, 5);
        Bookshelf bookshelf = new Bookshelf(5,5);
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
