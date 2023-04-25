package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

public class BookshelfRep {
    private final Item[][] bookshelf;
    private boolean change;

    public BookshelfRep(int rows, int columns) {
        bookshelf = new Item[rows][columns];
        change = false;
    }

    public Item[][] getBookshelf() {
        change = false;
        return bookshelf;
    }

    public boolean hasChanged() {
        return change;
    }

    public void updateRep(Position position, Item item) {
        change = true;
        bookshelf[position.getRow()][position.getColumn()] = item;
    }
}
