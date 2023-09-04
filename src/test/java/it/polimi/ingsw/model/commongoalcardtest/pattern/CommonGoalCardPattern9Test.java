package it.polimi.ingsw.model.commongoalcardtest.pattern;

import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPattern9;
import it.polimi.ingsw.model.commongoalcard.pattern.CommonGoalPatternInterface;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for common goal card pattern 9.
 */
public class CommonGoalCardPattern9Test {

    /**
     * Test with a Bookshelf full of the same type of {@code Item}s.
     */
    @Test
    public void testFullBookshelf(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test with 2 different groups of {@code Item}s, both correct.
     */
    @Test
    public void testDoubleCorrect(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i<2; i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        for(int i=2; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.BOOK,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * Test with a Bookshelf with each row composed of a different {@code Item}.
     */
    @Test
    public void testDifferentRows(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf();
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.CAT,j);
        }
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.BOOK,j);
        }
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.FRAME,j);
        }
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.PLANT,j);
        }
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.CUP,j);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
