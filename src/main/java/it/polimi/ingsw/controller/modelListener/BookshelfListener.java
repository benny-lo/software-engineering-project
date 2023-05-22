package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.HashMap;
import java.util.Map;

public class BookshelfListener extends ModelListener {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    public BookshelfListener(String owner) {
        super();
        this.owner = owner;
        bookshelf = new HashMap<>();
    }

    public Map<Position, Item> getBookshelf() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(bookshelf);

        bookshelf.clear();
        return ret;
    }

    public String getOwner() {
        return owner;
    }

    public void updateState(Position position, Item item) {
        changed = true;
        bookshelf.put(position, item);
    }
}
