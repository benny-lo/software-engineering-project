package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.HashSet;
import java.util.Set;

public class CommonGoalPattern5 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf)
    {
        int groups = 0;
        Set<Item> set = new HashSet<>();
        for(int i=0;i<bookshelf.getColumns()-1;i++)
        {
            set.clear();
            for(int j=0;j<bookshelf.getRows()-1;j++)
            {
                set.add(bookshelf.tileAt(i,j));
            }
            if(set.size() <= 3)
            {
                groups++;
            }
        }

        return groups >= 3;

    }
}
