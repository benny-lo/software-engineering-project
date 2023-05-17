package it.polimi.ingsw.view.modelListener;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.HashMap;
import java.util.Map;

public class LivingRoomListener extends ModelListener {
    private final Map<Position, Item> livingRoomChanges;

    public LivingRoomListener() {
        super();
        livingRoomChanges = new HashMap<>();
    }

    public Map<Position, Item> getLivingRoom() {
        changed = false;
        Map<Position, Item> ret = new HashMap<>(livingRoomChanges);

        livingRoomChanges.clear();
        return ret;
    }

    public void updateState(Position position, Item item) {
        changed = true;
        livingRoomChanges.put(position, item);
    }
}
