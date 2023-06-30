package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Message sent by the server when updates to the {@code Bookshelf} happen.
 */
public class BookshelfUpdate extends Message {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    /**
     * Constructor for the class. It sets the owner and bookshelf.
     * @param owner The player that owns the bookshelf.
     * @param bookshelf A {@code Map} of {@code Position} -> {@code Item} that changed.
     */
    public BookshelfUpdate(String owner, Map<Position, Item> bookshelf) {
        this.owner = owner;
        this.bookshelf = bookshelf;
    }

    /**
     * Getter for the owner of the bookshelf.
     * @return The player that owns the bookshelf.
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Getter for the updates to the {@code Bookshelf}.
     * @return The {@code Map} of updates.
     */
    public Map<Position, Item> getBookshelf() {
        return new HashMap<>(bookshelf);
    }
}
