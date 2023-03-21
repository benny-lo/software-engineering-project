package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern11;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern11Test {
    /**
     * test if the algorithm works properly
     */
    @Test
    public void correct(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf=new Bookshelf(6,5);
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
     * test if the algorithm works properly
     */
    @Test
    public void incorrect(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf=new Bookshelf(6,5);
        for(int i=bookshelf.getColumns()-1; i>=0; i--){
            for(int j=0; j<bookshelf.getColumns()-i-1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        for(int i=0; i<bookshelf.getColumns()-1; i++){
            bookshelf.insert(Item.BOOK, i);
        }
        bookshelf.insert(Item.FRAME, bookshelf.getColumns()-1);
        assertFalse(pattern.check(bookshelf));
    }
    /**
     * Test of pattern on an empty bookshelf.
     */
    @Test
    public void emptyBookshelf()
    {
        CommonGoalPatternInterface pattern = new CommonGoalPattern11();
        Bookshelf bookshelf = new Bookshelf(6,5);

        assertFalse(pattern.check(bookshelf));
    }
}
