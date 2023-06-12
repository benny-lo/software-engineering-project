package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern8;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommonGoalCardPattern8Test {
    /**
     * test if given a 3x3 Bookshelf full of CATs
     */
    @Test
    public void testCorrect3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * test if given a 6x5 Bookshelf with the first and the last column full of CATs
     */
    @Test
    public void testCorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf();
        for(int i=0; i< bookshelf.getRows(); i++){
                bookshelf.insert(Item.CAT,0);
        }
        for(int i=0; i< bookshelf.getRows(); i++){
            bookshelf.insert(Item.CAT,bookshelf.getColumns()-1);
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * test if given a 3x3 Bookshelf with the first column full of CATs and the last column full of BOOKs
     */
    @Test
    public void testIncorrect3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf();
        for(int j=0; j< bookshelf.getRows(); j++){
            bookshelf.insert(Item.CAT,0);
        }

        for(int i=0; i<bookshelf.getRows(); i++){
            bookshelf.insert(Item.BOOK,bookshelf.getColumns()-1);
        }
        assertFalse(pattern.check(bookshelf));
    }

    /**
     * test if a 6x5 Bookshelf with the first column full of CATs and the last column with the first tile a CAT
     * and the rests are BOOKs
     */
    @Test
    public void testIncorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf();
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
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void testEmptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }

}
