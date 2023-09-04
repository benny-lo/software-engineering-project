package it.polimi.ingsw.controller.modellistener;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener to the {@code LivingRoom}: it registers changes an any {@code Position} with the new
 * {@code Item} placed.
 */
public class LivingRoomListener extends ModelListener {
    private final Map<Position, Item> livingRoomUpdates;
    private final Map<Position, Item> livingRoomState;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     */
    public LivingRoomListener() {
        super();
        this.livingRoomUpdates = new HashMap<>();
        this.livingRoomState = new HashMap<>();
    }

    /**
     * Getter for the changes to the {@code LivingRoom}. The state of {@code this} is set to empty.
     * @return {@code Map} of the portion of the {@code LivingRoom} that changed.
     */
    public Map<Position, Item> getLivingRoomUpdates() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(livingRoomUpdates);
        livingRoomUpdates.clear();
        return ret;
    }

    /**
     * Getter for the full representation of the listened {@code LivingRoom}.
     * @return {@code Map} representing the contents of the {@code LivingRoom} in each cell.
     */
    public Map<Position, Item> getLivingRoomState() {
        return new HashMap<>(livingRoomState);
    }

    /**
     * Registers a change in the {@code LivingRoom}. The state of {@code this} is set to non-empty.
     * @param position The {@code Position} at which the changed occurred.
     * @param item The new {@code Item} that is now at {@code position} in the {@code LivingRoom}.
     */
    public void updateState(Position position, Item item) {
        changed = true;
        livingRoomUpdates.put(position, item);
        livingRoomState.put(position, item);
    }
}
