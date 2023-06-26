package it.polimi.ingsw.model.commonGoalCard.commonGoalPattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five tiles of the same type forming a diagonal.
 */

public class CommonGoalPattern11 implements CommonGoalPatternInterface{
    private static final int START = 0;
    private static final int END = 4;

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i=0; i<2; i++){
            if(bookshelf.tileAt(i,START)==bookshelf.tileAt(i+1,1) &&
                    bookshelf.tileAt(i,START)==bookshelf.tileAt(i+2,2) &&
                    bookshelf.tileAt(i,START)==bookshelf.tileAt(i+3,3) &&
                    bookshelf.tileAt(i,START)==bookshelf.tileAt(i+4,4) &&
                    bookshelf.tileAt(i,START)!=null) return true;
            if(bookshelf.tileAt(i,END)==bookshelf.tileAt(i+1,3) &&
                    bookshelf.tileAt(i,END)==bookshelf.tileAt(i+2,2) &&
                    bookshelf.tileAt(i,END)==bookshelf.tileAt(i+3,1) &&
                    bookshelf.tileAt(i,END)==bookshelf.tileAt(i+4,0)&&
                    bookshelf.tileAt(i,END)!=null) return true;
        }
        return false;
    }
}
