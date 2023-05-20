package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class BookshelfUpdate extends Message {
    private final String owner;
    private final Map<Position, Item> bookshelf;

    public BookshelfUpdate(String owner, Map<Position, Item> bookshelf) {
        this.owner = owner;
        this.bookshelf = bookshelf;
    }

    public String getOwner() {
        return owner;
    }

    public Map<Position, Item> getBookshelf() {
        return bookshelf;
    }
}
