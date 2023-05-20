package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class LivingRoomUpdate extends Message {
    private final Map<Position, Item> livingRoom;

    public LivingRoomUpdate(Map<Position, Item> livingRoom) {
        this.livingRoom = livingRoom;
    }

    public Map<Position, Item> getLivingRoomUpdate() {
        return livingRoom;
    }
}
