package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.HashMap;
import java.util.Map;

public class LivingRoomListener extends ModelListener {
    private final Map<Position, Item> livingRoomChanges;

    /**
     * Constructor for the class
     */
    public LivingRoomListener() {
        super();
        livingRoomChanges = new HashMap<>();
    }

    /**
     * Getter for the Living Room
     * @return - a map of the living room, with positions and items.
     */
    public Map<Position, Item> getLivingRoom() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(livingRoomChanges);

        livingRoomChanges.clear();
        return ret;
    }

    /**
     * This method updates the state of the living room, putting an item into a certain position
     * @param position - the position where the item needs to be put
     * @param item - the item that needs to be put.
     */
    public void updateState(Position position, Item item) {
        changed = true;
        livingRoomChanges.put(position, item);
    }
}
