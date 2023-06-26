package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

/**
 * Message sent by the server when the {@code LivingRoom} changes.
 */
public class LivingRoomUpdate extends Message {
    private final Map<Position, Item> livingRoom;

    /**
     * Constructor for the class. It sets the {@code Map} of changes to the {@code LivingRoom}.
     * @param livingRoom The updates to the {@code LivingRoom}.
     */
    public LivingRoomUpdate(Map<Position, Item> livingRoom) {
        this.livingRoom = livingRoom;
    }

    /**
     * Getter for the updates to the {@code LivingRoom}.
     * @return {@code Map} of updates to the {@code LivingRoom}.
     */
    public Map<Position, Item> getLivingRoomUpdate() {
        return livingRoom;
    }
}
