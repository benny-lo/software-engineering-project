package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class BookshelfUpdate extends NetworkMessage {
    private final String owner;
    private final Item[][] bookshelf;

    public BookshelfUpdate(String owner, Item[][] bookshelf) {
        this.owner = owner;
        this.bookshelf = bookshelf;
    }

    public String getOwner() {
        return owner;
    }

    public Item[][] getBookshelf() {
        return bookshelf;
    }
}
