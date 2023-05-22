package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.HashMap;
import java.util.Map;

public class BookshelvesListener extends ModelListener {
    private final Map<Position, Item> bookshelf;

    public BookshelvesListener() {
        super();
        bookshelf = new HashMap<>();
    }

    public Map<Position, Item> getBookshelf() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(bookshelf);

        bookshelf.clear();
        return ret;
    }

    public void updateState(Position position, Item item) {
        changed = true;
        bookshelf.put(position, item);
    }
}
