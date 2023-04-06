package it.polimi.ingsw.model.commonGoalCard.commonGoalPattern;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * eight or more tiles of the same type with no restrictions about the
 * position of these tiles
 */
public class CommonGoalPattern9 implements CommonGoalPatternInterface {
    private static final int MINIMUM = 8;
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counter=new int[Item.values().length];
        for(int i=0; i< bookshelf.getRows(); i++){
            for(int j=0; j< bookshelf.getColumns(); j++){
                if(bookshelf.tileAt(i,j)!=null) {
                    counter[bookshelf.tileAt(i, j).ordinal()]++;
                }
            }
        }
        for (int j : counter) {
            if (j >= MINIMUM) return true;
        }
        return false;
    }
}
