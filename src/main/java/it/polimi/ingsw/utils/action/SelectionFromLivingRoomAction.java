package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class SelectionFromLivingRoomAction extends Action {
    private final List<Position> positions;

    public SelectionFromLivingRoomAction(VirtualView view, List<Position> positions) {
        super(view);
        this.positions = positions;
    }

    public List<Position> getSelectedPositions() {
        return new ArrayList<>(positions);
    }
}
