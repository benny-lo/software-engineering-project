package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.List;

public class LivingRoomSelection extends Message {
    private final List<Position> positions;

    public LivingRoomSelection(List<Position> positions) {
        super();
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }
}
