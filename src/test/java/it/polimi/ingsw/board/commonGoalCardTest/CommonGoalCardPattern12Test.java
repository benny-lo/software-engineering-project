package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern12;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern12Test {
    /**
     * test given a 6x5 Bookshelf with 6 tiles in the first column and every column they decrease by 1
     */
    @Test
    public void correct6x5rightToLeft(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(6,5);
        for(int i=bookshelf.getColumns()-1; i>=0; i--){
            for(int j=0; j< bookshelf.getRows()-i; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * test given a 6x5 Bookshelf with 1 tile in the first column and every column they increase by 1
     */
    @Test
    public void correct6x5leftToRight(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(6,5);
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j<i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    /**
     * test given a 6x5 Bookshelf with 1 tile in the first column and every column they increase by 1,
     * but one more tile in the fourth column
     */
    @Test
    public void incorrect6x5leftToRight(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(6,5);
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j<i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        bookshelf.insert(Item.CAT, 3);
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * test given a 3x3 Bookshelf full of CATs
     */
    @Test
    public void incorrect3x3(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.CAT, j);
        }
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf = new Bookshelf(6,5);

        assertFalse(pattern.check(bookshelf));
    }
}
