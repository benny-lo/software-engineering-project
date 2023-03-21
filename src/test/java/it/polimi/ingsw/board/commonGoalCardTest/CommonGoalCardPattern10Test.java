package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern10;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern10Test {
    @Test
    public void correct(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                bookshelf.insert(Item.CAT,i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }
    @Test
    public void incorrect(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern10();
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
}
