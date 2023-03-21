package it.polimi.ingsw.board.commonGoalCardTest;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPattern12;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommonGoalCardPattern12Test {
    @Test
    public void correct(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int i=2; i>=0; i--){
            for(int j=0; j<2-i+1; j++){
                bookshelf.insert(Item.CAT, i);
            }
        }
        assertTrue(pattern.check(bookshelf));
    }

    @Test
    public void incorrect(){
        CommonGoalPatternInterface pattern = new CommonGoalPattern12();
        Bookshelf bookshelf=new Bookshelf(3,3);
        for(int j=0; j< bookshelf.getColumns(); j++){
            bookshelf.insert(Item.CAT, j);
        }
        assertFalse(pattern.check(bookshelf));
    }

}
