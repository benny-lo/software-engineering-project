package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.List;

public class LivingRoomSelection extends NetworkMessage {
    private final List<Position> positions;

    public LivingRoomSelection(List<Position> positions) {
        super();
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }
}
