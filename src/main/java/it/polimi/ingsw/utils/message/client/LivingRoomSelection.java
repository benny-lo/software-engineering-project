package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.ArrayList;
import java.util.List;

public class LivingRoomSelection extends Message {
    private final List<Position> positions;

    /**
     * Constructor for the class
     * @param positions - the position selected.
     */
    public LivingRoomSelection(List<Position> positions) {
        super();
        this.positions = new ArrayList<>(positions);
    }

    /**
     * Getter for the positions selected
     * @return - the positions selected
     */
    public List<Position> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return positions.toString();
    }
}
