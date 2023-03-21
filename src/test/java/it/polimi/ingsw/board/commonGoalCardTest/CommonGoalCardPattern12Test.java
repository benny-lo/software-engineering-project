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
     * test if the algorithm works properly
     */
    @Test
    public void correct(){
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
     * test if the algorithm works properly
     */
    @Test
    public void incorrect(){
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
