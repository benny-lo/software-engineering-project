package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class BookshelfUpdate extends Message {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    /**
     * Constructor for the class
     * @param owner - the player that owns the bookshelf
     * @param bookshelf - a map of the items on the bookshelf.
     */
    public BookshelfUpdate(String owner, Map<Position, Item> bookshelf) {
        this.owner = owner;
        this.bookshelf = bookshelf;
    }

    /**
     * Getter for the owner of the bookshelf
     * @return - the player that owns the bookshelf
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Getter for the map of the bookshelf
     * @return - the map of the items on the bookshelf.
     */
    public Map<Position, Item> getBookshelf() {
        return bookshelf;
    }
}
