package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

import java.util.List;

public class LivingRoomSelection extends NetworkMessageWithSender {
    private final List<Position> positions;

    public LivingRoomSelection(String nickname, List<Position> positions) {
        super(nickname);
        this.positions = positions;
    }

    public List<Position> getPositions() {
        return positions;
    }
}
