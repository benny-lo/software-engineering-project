package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.message.Message;

import java.util.List;

public class ItemsSelected extends Message {
    private final List<Item> items;

    /**
     * Constructor for the class.
     * @param items - list of items
     */
    public ItemsSelected(List<Item> items) {
        this.items = items;
    }
    /**
     * Getter for items
     * @return - the list of items.
     */
    public List<Item> getItems() {
        return items;
    }
}
