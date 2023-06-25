package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern8;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for common goal card pattern 8.
 */
public class CommonGoalCardPattern8Test {
    /**
     * Test with a full Bookshelf with all the same {@code Item}s.
     */
    @Test
    public void testFullBookshelf(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test with a Bookshelf with all the same {@code Item}s in the corners.
     */
    @Test
    public void testEqualItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0; i< bookshelf.getRows(); i++){
                bookshelf.insert(Item.CAT,0);
        }
        for(int i=0; i< bookshelf.getRows(); i++){
            bookshelf.insert(Item.CAT,bookshelf.getColumns()-1);
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test with a Bookshelf with two different {@code Item}s in the corners.
     */
    @Test
    public void testTwoDifferentItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();
        for(int j=0; j< bookshelf.getRows(); j++){
            bookshelf.insert(Item.CAT,0);
        }

        for(int i=0; i<bookshelf.getRows(); i++){
            bookshelf.insert(Item.BOOK,bookshelf.getColumns()-1);
        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test with a Bookshelf with a different {@code Item} in one corner.
     */
    @Test
    public void testOneDifferentItem(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();
        for(int j=0; j< bookshelf.getRows(); j++){
            bookshelf.insert(Item.CAT,0);
        }

        bookshelf.insert(Item.CAT,bookshelf.getColumns()-1);

        for(int i=0; i<bookshelf.getRows()-1; i++){
            bookshelf.insert(Item.BOOK,bookshelf.getColumns()-1);
        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }

}
