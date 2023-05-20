package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.HashMap;
import java.util.Map;

public class BookshelvesListener extends ModelListener {
    private final Map<String, Map<Position, Item>> bookshelves;

    public BookshelvesListener() {
        super();
        bookshelves = new HashMap<>();
    }

    public Map<String, Map<Position, Item>> getBookshelves() {
        changed = false;
        Map<String, Map<Position, Item>> ret = new HashMap<>();
        for(String nick : bookshelves.keySet()) {
            ret.put(nick, new HashMap<>(bookshelves.get(nick)));
        }

        bookshelves.clear();
        return ret;
    }

    public void updateState(String owner, Position position, Item item) {
        changed = true;
        if (!bookshelves.containsKey(owner)) bookshelves.put(owner, new HashMap<>());
        bookshelves.get(owner).put(position, item);
    }
}
