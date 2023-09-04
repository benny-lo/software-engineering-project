package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.utils.Item;

/**
 * Class representing the following pattern:
 * five tiles of the same type forming a diagonal.
 */

public class CommonGoalPattern11 implements CommonGoalPatternInterface{
    private static final int LEN = 5;

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        for(int i = 0; i < bookshelf.getRows(); i++){
            for(int j = 0; j < bookshelf.getColumns(); j++) {

                if (bookshelf.tileAt(i, j) == null) continue;

                if (
                        checkDiagonal(i, j, bookshelf, 1) ||
                        checkDiagonal(i, j, bookshelf, -1)
                ) return true;
            }
        }
        return false;
    }

    private boolean validDimensions(int i, int j, Bookshelf bookshelf) {
        return i >= 0 && i < bookshelf.getRows() && j >= 0 && j < bookshelf.getColumns();
    }

    private boolean checkDiagonal(int i, int j, Bookshelf bookshelf, int dirRow) {
        Item item = bookshelf.tileAt(i, j);

        for(int a = 1; a < LEN; a++) {
            i += dirRow;
            j += 1;

            if (!validDimensions(i, j, bookshelf)) return false;
            if (bookshelf.tileAt(i, j) != item) return false;
        }

        return true;
    }


}
