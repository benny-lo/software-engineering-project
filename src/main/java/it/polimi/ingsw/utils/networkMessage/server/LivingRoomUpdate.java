package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class LivingRoomUpdate extends NetworkMessage {
    private final Item[][] livingRoom;

    public LivingRoomUpdate(Item[][] livingRoom) {
        this.livingRoom = livingRoom;
    }

    public Item[][] getLivingRoom() {
        return livingRoom;
    }
}
