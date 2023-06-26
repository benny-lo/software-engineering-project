package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern10;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern8;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for common goal card pattern 10.
 */
public class CommonGoalCardPattern10Test {
    /**
     * Test with a Bookshelf full of the same type of {@code Item}s.
     */
    @Test
    public void testFullBookshelf(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test with 3 adjacent columns, only two have the same {@code Item}s.
     */
    @Test
    public void testTwoColumnsWithSameItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int j=0; j< bookshelf.getRows(); j++){
            bookshelf.insert(Item.CAT,0);
        }

        for(int i=0; i<bookshelf.getRows(); i++){
            bookshelf.insert(Item.BOOK,bookshelf.getColumns()-1);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test with columns filled with alternating {@code Item}s.
     */
    @Test
    public void testColumnsWithAlternatingItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                if(i%2==0) bookshelf.insert(Item.CAT,i);
                else bookshelf.insert(Item.BOOK,i);
            }
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test with an X pattern on the upper corner.
     */
    @Test
    public void testXUpperCorner(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                if(i%2==0) bookshelf.insert(Item.CAT,i);
                else if(i==3 && j==4) bookshelf.insert(Item.CAT,i);
                else bookshelf.insert(Item.BOOK,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
