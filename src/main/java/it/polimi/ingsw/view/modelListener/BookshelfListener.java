package it.polimi.ingsw.view.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class BookshelfListener extends ModelListener {
    private final Map<Pair<String, Position>, Item> bookshelves;

    public BookshelfListener() {
        super();
        bookshelves = new HashMap<>();
    }

    public Map<Pair<String, Position>, Item> getBookshelves() {
        changed = false;
        Map<Pair<String,Position>, Item> ret = new HashMap<>(bookshelves);

        bookshelves.clear();
        return ret;
    }

    public void updateState(String owner, Position position, Item item) {
        changed = true;
        bookshelves.put(new Pair<>(owner, position), item);
    }
}
