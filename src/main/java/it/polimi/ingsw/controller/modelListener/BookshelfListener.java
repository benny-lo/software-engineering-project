package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;

import java.util.HashMap;
import java.util.Map;

public class BookshelfListener extends ModelListener {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    /**
     * Constructor for the class
     * @param owner The owner of the Bookshelf
     */
    public BookshelfListener(String owner) {
        super();
        this.owner = owner;
        bookshelf = new HashMap<>();
    }

    /**
     * Getter for the bookshelf
     * @return A Map composed of positions and items that compose the bookshelf.
     */
    public Map<Position, Item> getBookshelf() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(bookshelf);

        bookshelf.clear();
        return ret;
    }

    /**
     * Getter for the owner
     * @return The owner of the bookshelf.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * This method updates the state of the bookshelf.
     * @param position The position in which the item is supposed to be put
     * @param item The item to be put.
     */
    public void updateState(Position position, Item item) {
        changed = true;
        bookshelf.put(position, item);
    }
}
