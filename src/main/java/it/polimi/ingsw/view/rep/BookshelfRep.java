package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

public class BookshelfRep extends Rep {
    private final Item[][] bookshelf;

    public BookshelfRep(int rows, int columns) {
        super();
        bookshelf = new Item[rows][columns];
    }

    public Item[][] getBookshelf() {
        peek();
        return bookshelf;
    }

    public void updateRep(Position position, Item item) {
        update();
        bookshelf[position.getRow()][position.getColumn()] = item;
    }
}
