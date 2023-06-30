package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.Message;

import java.util.List;

/**
 * Message sent by the client representing a selection of {@code Position}s from the {@code LivingRoom}.
 */
public class LivingRoomSelection extends Message {
    private final List<Position> positions;

    /**
     * Constructor for the class. It sets the positions.
     * @param positions The position selected.
     */
    public LivingRoomSelection(List<Position> positions) {
        super();
        this.positions = positions;
    }

    /**
     * Getter for the positions selected.
     * @return The positions selected.
     */
    public List<Position> getPositions() {
        return positions;
    }

    /**
     * {@code String} of the {@code List} of {@code Position}s.
     * @return The {@code String} corresponding to {@code this}.
     */
    @Override
    public String toString() {
        return positions.toString();
    }
}
