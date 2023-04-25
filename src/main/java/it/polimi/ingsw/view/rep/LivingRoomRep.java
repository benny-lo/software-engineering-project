package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

public class LivingRoomRep {
    private final Item[][] livingRoom;
    private boolean change;

    public LivingRoomRep(int rows, int columns) {
        livingRoom = new Item[rows][columns];
        change = false;
    }

    public boolean hasChanged() {
        return change;
    }

    public Item[][] getLivingRoom() {
        change = false;
        return livingRoom;
    }

    public void updateRep(Position position, Item item) {
        change = true;
        livingRoom[position.getRow()][position.getColumn()] = item;
    }
}
