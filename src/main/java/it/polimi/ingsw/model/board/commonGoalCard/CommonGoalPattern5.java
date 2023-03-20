package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern5 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf)
    {
        int groups = 0;
        int different = 0;
        for(int j=0;j<bookshelf.getColumns();j++)
        {
            for (int i = 0; i < bookshelf.getRows(); i++)
            {
                if(bookshelf.tileAt(i,j) != bookshelf.tileAt(i+1,j))
                {
                    different++;
                }

            }
            if(different <= 3)
            {
                groups++;
            }
        }

        return groups >= 3;

    }
}
