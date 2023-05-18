package it.polimi.ingsw.view.modelListener;

import it.polimi.ingsw.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsChosenListener extends ModelListener {
    private final String owner;
    private List<Item> itemsChosen;
    public ItemsChosenListener(String owner) {
        super();
        this.owner = owner;
        itemsChosen = null;
    }

    public String getOwner() {
        return owner;
    }

    public List<Item> getItemsChosen() {
        changed = false;
        if (itemsChosen == null) return null;

        List<Item> ret = new ArrayList<>(itemsChosen);
        itemsChosen = null;
        return ret;
    }

    public void updateState(List<Item> itemsChosen) {
        changed = true;
        this.itemsChosen = itemsChosen;
    }
}
