package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * Six disjoint groups of two {@code Item}s of the same kind
 * forming a 1*2 rectangle. The {@code Item}s of the six
 *  groups do not have to be of the same kind.
 */

public class CommonGoalPattern4 implements CommonGoalPatternInterface{
    @Override

    public boolean check(Bookshelf bookshelf)
    {
        int groups = 0;
        boolean[][] disjoint = new boolean[bookshelf.getRows()][bookshelf.getColumns()];
        for(int i=0;i<bookshelf.getRows();i++)
        {
            for(int j=0;j<bookshelf.getColumns();j++)
            {
                disjoint[i][j] = false;
            }
        }
        for(int i=0; i<bookshelf.getRows()-1;i++)
        {
            for(int j=0;j<bookshelf.getColumns()-1;j++)
            {
                if (bookshelf.tileAt(i, j) == bookshelf.tileAt(i, j + 1) && not_already(disjoint,i,j) && not_already(disjoint,i,j+1))
                {
                    groups++;
                    disjoint[i][j] = true;
                    disjoint[i][j+1] = true;
                }
            }
        }

        if(groups >= 6)
        {
            return true;
        }
        else
        {
            for(int j=0;j<bookshelf.getColumns()-1;j++)
            {
                for(int i=0;i<bookshelf.getRows()-1;i++)
                {
                    if(bookshelf.tileAt(i,j)==bookshelf.tileAt(i+1,j) && not_already(disjoint,i,j) && not_already(disjoint,i+1,j))
                    {
                        groups++;
                        disjoint[i][j] = true;
                        disjoint[i+1][j] = true;
                    }
                }
            }
        }

        return groups >= 6;







    }

    private boolean not_already(boolean[][] array,int x,int y)
    {
        return !array[x][y];
    }
}
