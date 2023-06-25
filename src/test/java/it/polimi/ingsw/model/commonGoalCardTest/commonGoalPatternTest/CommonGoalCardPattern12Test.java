package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern12;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for common goal card pattern 12.
 */
public class CommonGoalCardPattern12Test {
    /**
     * Test with 6 {@code Item}s in the first column and every column's height decreases by 1.
     */
    @Test
    public void testLeftToRightDecreasedBy1(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=bookshelf.getColumns()-1; i>=0; i--){
            for(int j=0; j< bookshelf.getRows()-i; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test with 1 {@code Item} in the first column and every column's height increases by 1.
     */
    @Test
    public void testLeftToRightIncreasedBy1(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j<i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test with 1 {@code Item} in the first column and every column's height increases by 1, but the fourth column has too many {@code Item}s.
     */
    @Test
    public void testLeftToRightIncreasedBy1WithMistake(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j<i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        bookshelf.insert(Item.CAT, 3);
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test on a full Bookshelf.
     */
    @Test
    public void testFullBookshelf(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf();
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.CAT, j);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
