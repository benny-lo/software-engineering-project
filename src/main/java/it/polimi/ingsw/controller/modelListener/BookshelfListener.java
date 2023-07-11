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
    private final Map<Position, Item> bookshelfUpdates;
    private final Map<Position, Item> bookshelfState;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     * @param owner The owner of the Bookshelf to listen to.
     */
    public BookshelfListener(String owner) {
        super();
        this.owner = owner;
        this.bookshelfUpdates = new HashMap<>();
        this.bookshelfState = new HashMap<>();
    }

    /**
     * Getter for the latest updates to the listened {@code Bookshelf}. The state of {@code this}
     * is set to empty.
     * @return {@code Map} of the portion of the {@code Bookshelf} that changed.
     */
    public Map<Position, Item> getBookshelfUpdates() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(bookshelfUpdates);
        bookshelfUpdates.clear();
        return ret;
    }

    /**
     * Getter for the full representation of the listened {@code Bookshelf}.
     * @return {@code Map} representing the entire {@code Bookshelf}.
     */
    public Map<Position, Item> getBookshelfState() {
        return new HashMap<>(bookshelfState);
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
        bookshelfUpdates.put(position, item);
        bookshelfState.put(position, item);
    }
}
