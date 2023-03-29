package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern8;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommonGoalCardPattern8Test {
    /**
     * test if given a 3x3 Bookshelf full of CATs
     */
    @Test
    public void correct3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(3,3);
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
    public void correct6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(6,5);
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
    public void incorrect3x3(){
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
     * test if a 6x5 Bookshelf with the first column full of CATs and the last column with the first tile a CAT
     * and the rests are BOOKs
     */
    @Test
    public void incorrect6x5(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(6,5);
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
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf = new Bookshelf(6,5);

        assertFalse(pattern.check(bookshelf));
    }

}
