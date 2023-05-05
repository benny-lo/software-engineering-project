package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.List;

public class ItemsSelected extends NetworkMessage {
    private final List<Item> items;

    public ItemsSelected(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}
