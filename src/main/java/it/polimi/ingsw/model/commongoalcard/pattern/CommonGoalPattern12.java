package it.polimi.ingsw.model.commongoalcard.pattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five columns of increasing or decreasing height. starting from the
 * first column on the left or on the right, each next column
 * must be made of exactly one more tile. tiles can be of any type.
 */
public class CommonGoalPattern12 implements CommonGoalPatternInterface {
    private static final int NUMBER_OF_COLUMNS = 5;

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counter = new int[bookshelf.getColumns()];

        for(int i = 0; i < bookshelf.getRows(); i++){
            for(int j = 0; j < bookshelf.getColumns(); j++){
                if(bookshelf.tileAt(i, j) != null){
                    counter[j]++;
                }
            }
        }

        for(int i = 0; i < counter.length - NUMBER_OF_COLUMNS + 1; i++) {
            boolean increasing = true;
            boolean decreasing = true;

            if (counter[i] == 0) continue;

            for(int j = 1; j < NUMBER_OF_COLUMNS; j++) {
                int tmp = counter[i + j] - counter[i];

                if (tmp != j) increasing = false;
                if (tmp != -j || counter[i+j] == 0) decreasing = false;
            }

            if (increasing || decreasing) return true;
        }

        return false;
    }
}
