package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern11;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern11Test {
    /**
     * test given a 6x5 Bookshelf with a full diagonal of BOOKs from the first right tile to upper left
     */
    @Test
    public void correct6x5rightToLeft(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf=new Bookshelf();
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
     * test given a 6x5 Bookshelf with a full diagonal of BOOKs from the second lower left tile (0,1) to upper right
     */
    @Test
    public void correct6x5leftToRight(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf=new Bookshelf();
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
     * test given a 6x5 Bookshelf without a full diagonal from left to upper right
     */
    @Test
    public void incorrect6x5(){
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
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf = new Bookshelf();

        assertFalse(pattern.check(bookshelf));
    }
}
