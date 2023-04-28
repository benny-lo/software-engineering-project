package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.Item;

import java.util.List;

public class ItemsChosenRep extends Rep {
    private final String owner;
    private List<Item> itemsChosen;
    public ItemsChosenRep(String owner) {
        super();
        this.owner = owner;
        itemsChosen = null;
    }

    public String getOwner() {
        return owner;
    }

    public List<Item> getItemsChosen() {
        peek();
        return itemsChosen;
    }

    public void updateRep(List<Item> itemsChosen) {
        update();
        this.itemsChosen = itemsChosen;
    }
}
