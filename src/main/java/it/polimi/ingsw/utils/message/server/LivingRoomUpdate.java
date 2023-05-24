package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class LivingRoomUpdate extends Message {
    private final Map<Position, Item> livingRoom;

    /**
     * Constructor for the class
     * @param livingRoom - living room updated.
     */
    public LivingRoomUpdate(Map<Position, Item> livingRoom) {
        this.livingRoom = livingRoom;
    }
    /**
     * Getter for the living room situation
     * @return - the updated version of the living room.
     */
    public Map<Position, Item> getLivingRoomUpdate() {
        return livingRoom;
    }
}
