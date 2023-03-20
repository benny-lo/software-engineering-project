package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern10 implements CommonGoalPatternInterface{

    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i=0; i<6-2; i++){
            for(int j=0; j<5-2; j++){
                if(bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+1,j+1) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i,j+2) &&
                        bookshelf.tileAt(i,j)==bookshelf.tileAt(i+2,j+2)) return true;
            }
        }
        return false;
    }
}
