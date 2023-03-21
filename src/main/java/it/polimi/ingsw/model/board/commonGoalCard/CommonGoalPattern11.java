package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five tiles of the same type forming a diagonal
 */

public class CommonGoalPattern11 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i=0; i<2; i++){
            if(bookshelf.tileAt(i,0)==bookshelf.tileAt(i+1,1) &&
                    bookshelf.tileAt(i,0)==bookshelf.tileAt(i+2,2) &&
                    bookshelf.tileAt(i,0)==bookshelf.tileAt(i+3,3) &&
                    bookshelf.tileAt(i,0)==bookshelf.tileAt(i+4,4) &&
                    bookshelf.tileAt(i,0)!=null) return true;
            if(bookshelf.tileAt(i,4)==bookshelf.tileAt(i+1,3) &&
                    bookshelf.tileAt(i,4)==bookshelf.tileAt(i+2,2) &&
                    bookshelf.tileAt(i,4)==bookshelf.tileAt(i+3,1) &&
                    bookshelf.tileAt(i,4)==bookshelf.tileAt(i+4,0)&&
                    bookshelf.tileAt(i,4)!=null) return true;
        }
        return false;
    }
}
