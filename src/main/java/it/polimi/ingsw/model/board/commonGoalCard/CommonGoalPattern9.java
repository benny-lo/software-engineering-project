package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * eight or more tiles of the same type with no restrictions about the
 * position of these tiles
 */
public class CommonGoalPattern9 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counter=new int[Item.values().length];
        for(int i=0; i< bookshelf.getRows(); i++){
            for(int j=0; j< bookshelf.getColumns(); j++){
                counter[bookshelf.tileAt(i, j).ordinal()]++;
            }
        }
        for(int i = 0; i < counter.length; i++){
            if(counter[i] >= 8) return true;
        }
        return false;
    }
}
