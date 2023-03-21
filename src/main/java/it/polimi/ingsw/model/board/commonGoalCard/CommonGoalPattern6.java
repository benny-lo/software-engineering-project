package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.model.Item;

import java.util.HashSet;
import java.util.Set;
/**
 * Class representing the following pattern:
 * Two rows of {@code Item}s of 5 different kinds
 */
public class CommonGoalPattern6 implements CommonGoalPatternInterface{
    @Override

    public boolean check(Bookshelf bookshelf)
    {
        int groups = 0;
        Set<Item> set = new HashSet<>();
        for(int i=0;i<bookshelf.getRows();i++)
        {
            set.clear();
            for(int j=0;j<bookshelf.getColumns();j++)
            {
                if(bookshelf.tileAt(i,j) != null)
                {
                    set.add(bookshelf.tileAt(i,j));
                }
            }
            if(set.size() == 5)
            {
                groups++;
            }
        }
        return groups >= 2;

    }
}
