package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * four tiles of the same type in the four corners of the bookshelf
 */
public class CommonGoalPattern8 implements CommonGoalPatternInterface {
    private static final int START = 0;
    @Override
    public boolean check(Bookshelf bookshelf) {
        if(bookshelf.tileAt(START,START)==bookshelf.tileAt(START, bookshelf.getColumns()-1) &&
                bookshelf.tileAt(START,START)==bookshelf.tileAt(bookshelf.getRows()-1,START) &&
                bookshelf.tileAt(START,START)==bookshelf.tileAt(bookshelf.getRows()-1,bookshelf.getColumns()-1) &&
                bookshelf.tileAt(START,START)!=null){
            return true;
        }
        return false;
    }
}
