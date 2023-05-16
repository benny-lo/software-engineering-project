package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.Map;

public class LivingRoomUpdate extends NetworkMessage {
    private final Map<Position, Item> livingRoom;

    public LivingRoomUpdate(Map<Position, Item> livingRoom) {
        this.livingRoom = livingRoom;
    }

    public Map<Position, Item> getLivingRoomUpdate() {
        return livingRoom;
    }
}
