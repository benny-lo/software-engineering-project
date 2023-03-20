package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

import java.util.HashSet;
import java.util.Set;

import it.polimi.ingsw.model.Item;
/**
 * Class representing the following pattern:
 * Four full rows of  {@code Item}s of up to three different kind
 * The {@code Item}s of the four rows do not have to be of the same kind.
 */
public class CommonGoalPattern7 implements CommonGoalPatternInterface{
    @Override

    public boolean check(Bookshelf bookshelf)
    {
        Set<Item> set = new HashSet<>();
        int groups = 0;
        for(int i=0;i<bookshelf.getRows();i++)
        {
            set.clear();
            for(int j=0;j<bookshelf.getColumns();j++)
            {
                set.add(bookshelf.tileAt(i,j));
            }
            if(set.size() <= 3)
            {
                groups++;
            }
        }

        return groups >= 4;
    }
}
