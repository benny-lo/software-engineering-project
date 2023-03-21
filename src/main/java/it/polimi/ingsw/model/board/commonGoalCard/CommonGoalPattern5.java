package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.HashSet;
import java.util.Set;

/**
 * Class representing the following pattern:
 * Three full columns of  {@code Item}s of up to 3 different kinds
 * The {@code Item}s of the three columns do not have to be of the same kind.
 */
public class CommonGoalPattern5 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf)
    {
        int groups = 0;
        Set<Item> set = new HashSet<>();

        for(int i=0;i<bookshelf.getColumns()-1;i++)
            if (bookshelf.isFullCol(bookshelf, i)) {
                set.clear();
                for (int j = 0; j < bookshelf.getRows() - 1; j++) {
                    if (bookshelf.tileAt(j, i) != null) {
                        set.add(bookshelf.tileAt(j, i));
                    }
                }
                if (set.size() <= 3 && set.size() > 0) {
                    groups++;
                }
            }


        return groups >= 3;

    }


}
