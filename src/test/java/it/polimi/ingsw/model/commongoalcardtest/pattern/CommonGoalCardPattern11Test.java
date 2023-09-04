package it.polimi.ingsw.model.commongoalcardtest.pattern;

import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPattern11;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for common goal card pattern 11.
 */
public class CommonGoalCardPattern11Test {
    /**
     * Test with a diagonal from right to left composed of 5 equal {@code Item}s.
     */
    @Test
    public void testRightToLeftDiagonalWith5EqualItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf = new Bookshelf();
        for(int i=bookshelf.getColumns()-1; i>=0; i--){
            for(int j=0; j<bookshelf.getColumns()-i; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        for(int i=0; i<bookshelf.getColumns(); i++){
            bookshelf.insert(Item.BOOK, i);
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test with a diagonal from left to right composed of 5 equal {@code Item}s.
     */
    @Test
    public void testLeftToRightDiagonalWith5EqualItems(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf = new Bookshelf();
        for(int i=0; i<bookshelf.getColumns(); i++){
            for(int j=0; j<i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        for(int i=0; i<bookshelf.getColumns(); i++){
            bookshelf.insert(Item.BOOK, i);
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test without a full diagonal from left to right.
     */
    @Test
    public void testWithoutFullDiagonal(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=1; i<bookshelf.getColumns()-1; i++){
            for(int j=0; j<i; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        for(int i=0; i<bookshelf.getColumns()-1; i++){
            bookshelf.insert(Item.BOOK, i);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
