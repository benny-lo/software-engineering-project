package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternCountGroups;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for Common Goal Card Pattern 4.
 */

public class CommonGoalCardPattern4Test {
    /**
     * Test of pattern on exactly 6 adjacent couples
     */
    @Test
    public void exactlySix()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,6, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf(6,6);
        for(int j=0;j<2;j++)
        {
            for (int i = 0; i < bookshelf.getColumns(); i++) {
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));

    }
    /**
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,6, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf(6,6);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with less than 6 adjacent couples
     */
    @Test
    public void lessThanSix()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,6, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf(6,6);
        bookshelf.insert(Item.CAT,0);
        bookshelf.insert(Item.CAT,1);

        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with 6 groups with items in common
     */
    @Test
    public void commonItems()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,6, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf(6,6);
        for (int i = 0; i < bookshelf.getColumns(); i++)
        {
            bookshelf.insert(Item.CAT, i);
        }
        for(int i=0; i< bookshelf.getColumns()/2;i++)
        {
            bookshelf.insert(Item.CAT, i);
        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test of pattern on a bookshelf with 12 not adjacent items of the same kind
     */
    @Test
    public void twelveNotAdjacent()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,6, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf(6,6);
        for(int j=0;j<2;j++)
        {
            for(int i=0;i<bookshelf.getColumns()/2;i++)
            {
                bookshelf.insert(Item.CAT,i);
                bookshelf.insert(Item.BOOK,i+1);
                i++;
            }

            for(int n=0;n<bookshelf.getColumns()/2;n++)
            {
                bookshelf.insert(Item.FRAME,n);
                bookshelf.insert(Item.CAT,n+1);
                n++;
            }
        }

        assertFalse(pattern.check(bookshelf));


    }

}
