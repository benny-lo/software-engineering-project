package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern8;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommonGoalCardPattern8Test {
    /**
     * test if the algorithm works correctly
     */
    @Test
    public void correct(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }

    /**
     * test if the algorithm works correctly
     */
    @Test
    public void incorrect(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern8();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=0; i<2; i++){
            for(int j=0; j<3; j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        for(int i=0; i<3; i++){
            bookshelf.insert(Item.BOOK,2);
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
