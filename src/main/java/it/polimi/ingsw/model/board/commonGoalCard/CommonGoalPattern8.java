package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern8 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        if(bookshelf.tileAt(0,0)==bookshelf.tileAt(0,4) &&
                bookshelf.tileAt(0,0)==bookshelf.tileAt(5,0) &&
                bookshelf.tileAt(0,0)==bookshelf.tileAt(5,4) ){
            return true;
        }
        return false;
    }
}
