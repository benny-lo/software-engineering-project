package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern8 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        if(bookshelf.tileAt(0,0)==bookshelf.tileAt(0, bookshelf.getColumns()-1) &&
                bookshelf.tileAt(0,0)==bookshelf.tileAt(bookshelf.getRows()-1,0) &&
                bookshelf.tileAt(0,0)==bookshelf.tileAt(bookshelf.getRows()-1,bookshelf.getColumns()-1) ){
            return true;
        }
        return false;
    }
}
