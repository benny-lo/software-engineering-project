package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;

public class SelectionFromLivingRoomAction extends Action {
    private final List<Position> positions;

    public SelectionFromLivingRoomAction(String nickname, List<Position> positions) {
        super(nickname);
        this.positions = positions;
    }

    public List<Position> getSelectedPositions() {
        return new ArrayList<>(positions);
    }
}
