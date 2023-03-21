package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five tiles of the same type forming an X
 */

public class CommonGoalPattern10 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i=0; i< bookshelf.getRows()-2; i++){
            for(int j=0; j< bookshelf.getColumns()-2; j++){
                if(bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+1,j+1) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i,j+2) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j+2) &&
                        bookshelf.tileAt(i,j)!=null) return true;
            }
        }
        return false;
    }
}
