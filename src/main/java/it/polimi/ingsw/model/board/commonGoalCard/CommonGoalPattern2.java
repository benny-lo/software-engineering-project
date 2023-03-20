package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.model.Item;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class representing the following pattern:
 * Two distinct columns, each containing 6 distinct items.
 */
public class CommonGoalPattern2 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        int counter = 0;
        for(int j = 0; j < bookshelf.getColumns(); j++) {
            if (checkSingleColumn(j, bookshelf)) counter++;
            if (counter == 2) return true;
        }
        return false;
    }

    /**
     * Check if {@code column} contains at least 6 different types.
     * @param column the column to check.
     * @return {@code true} iff {@code column} contains 6 different types.
     */
    private boolean checkSingleColumn(int column, Bookshelf bookshelf){
        Set<Item> elements = new HashSet<>();
        for(int i = 0; i < bookshelf.getRows(); i++) {
            if (bookshelf.tileAt(i, column) == null) continue;
            elements.add(bookshelf.tileAt(i, column));
        }
        return elements.size() >= Stream.of(Item.values()).filter((item) -> item != Item.LOCKED).toList().size();
    }
}
