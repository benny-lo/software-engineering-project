package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener to the {@code Bookshelf}. It listens for updates to the {@code Item}s and
 * records for every {@code Position} the new {@code Item} (maybe {@code null}).
 */
public class BookshelfListener extends ModelListener {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     * @param owner The owner of the Bookshelf to listen to.
     */
    public BookshelfListener(String owner) {
        super();
        this.owner = owner;
        bookshelf = new HashMap<>();
    }

    /**
     * Getter for the latest updates to the listened {@code Bookshelf}. The state of {@code this}
     * is set to empty.
     * @return {@code Map} of the portion of the {@code Bookshelf} that changed.
     */
    public Map<Position, Item> getBookshelf() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(bookshelf);

        bookshelf.clear();
        return ret;
    }

    /**
     * Getter for the owner of the listened {@code Bookshelf}.
     * @return The owner of the {@code Bookshelf}.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Registers a change in the listened {@code Bookshelf}. The state of {@code this} is set to
     * non-empty.
     * @param position The {@code Position} that changed.
     * @param item The {@code Item} at {@code position}.
     */
    public void updateState(Position position, Item item) {
        changed = true;
        bookshelf.put(position, item);
    }
}
