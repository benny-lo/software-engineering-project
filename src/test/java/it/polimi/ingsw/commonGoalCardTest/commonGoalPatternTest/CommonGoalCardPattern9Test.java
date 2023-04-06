package it.polimi.ingsw.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern9;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern9Test {

    /**
     * test if given a 3x3 Bookshelf full of CATs
     */
    @Test
    public void correct3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * test if given a 6x5 Bookshelf with the first two columns full of CATs and the rest is full of BOOKs, 2 correct groups
     */
    @Test
    public void doublecorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf(6,5);
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
     * test if given a 6x5 Bookshelf with each row a different item
     */
    @Test
    public void nonecorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf=new Bookshelf(6,5);
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
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern9();
        Bookshelf bookshelf = new Bookshelf(6,5);

        assertFalse(pattern.check(bookshelf));
    }
}
