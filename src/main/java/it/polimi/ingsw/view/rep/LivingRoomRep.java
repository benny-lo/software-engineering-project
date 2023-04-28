package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

public class LivingRoomRep extends Rep {
    private final Item[][] livingRoom;

    public LivingRoomRep(int rows, int columns) {
        super();
        livingRoom = new Item[rows][columns];
    }

    public Item[][] getLivingRoom() {
        peek();
        return livingRoom;
    }

    public void updateRep(Position position, Item item) {
        update();
        livingRoom[position.getRow()][position.getColumn()] = item;
    }
}
