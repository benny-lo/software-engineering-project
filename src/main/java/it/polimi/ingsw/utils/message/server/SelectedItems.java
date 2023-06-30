package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message sent by the server when somebody selects {@code Item}s from the {@code LivingRoom}.
 */
public class SelectedItems extends Message {
    private final List<Item> items;

    /**
     * Constructor for the class. It sets the {@code List} of selected {@code Item}s.
     * @param items - list of items
     */
    public SelectedItems(List<Item> items) {
        this.items = items;
    }

    /**
     * Getter for the {@code List} of selected {@code Item}s.
     * @return The list of selected {@code Item}s.
     */
    public List<Item> getItems() {
        return (items != null) ?
                new ArrayList<>(items) :
                null;
    }
}
