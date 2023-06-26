package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing a listener to the {@code LivingRoom} of a {@code Game}.
 */
public class LivingRoomListener extends ModelListener {
    private final Map<Position, Item> livingRoomChanges;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     */
    public LivingRoomListener() {
        super();
        livingRoomChanges = new HashMap<>();
    }

    /**
     * Getter for the changes to the {@code LivingRoom}. The state of {@code this} is set to empty.
     * @return {@code Map} of the portion of the {@code LivingRoom} that changed.
     */
    public Map<Position, Item> getLivingRoom() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(livingRoomChanges);

        livingRoomChanges.clear();
        return ret;
    }

    /**
     * Registers a change in the {@code LivingRoom}. The state of {@code this} is set to non-empty.
     * @param position The {@code Position} at which the changed occurred.
     * @param item The new {@code Item} that is now at {@code position} in the {@code LivingRoom}.
     */
    public void updateState(Position position, Item item) {
        changed = true;
        livingRoomChanges.put(position, item);
    }
}
