package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern10;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern8;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern10Test {
    /**
     * test if given a 3x3 Bookshelf full of CATs
     */
    @Test
    public void testCorrect3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * test if given a 6x5 Bookshelf full of CATs
     */
    @Test
    public void testCorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * test if given a 2x2 Bookshelf full of CATs
     */
    @Test
    public void testIncorrect2x2(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf = new Bookshelf(2, 2);
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * test if given a 3x3 Bookshelf with the first column full of CATs and the last column full of BOOKs
     */
    @Test
    public void testIncorrect3x3(){
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
     * test if given a 6x5 Bookshelf with even columns full of CATs and odd columns full of BOOKs
     */
    @Test
    public void testIncorrect6x5(){
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
     * test if given a 6x5 Bookshelf with even columns full of CATs and odd columns full of BOOKs, but an X in the upper corner
     */
    @Test
    public void testUpperCornerCorrect6x5(){
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
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
