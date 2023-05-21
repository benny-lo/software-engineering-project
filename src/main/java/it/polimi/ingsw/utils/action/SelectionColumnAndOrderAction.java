package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class SelectionColumnAndOrderAction extends Action {
    private final int column;
    private final List<Integer> order;

    public SelectionColumnAndOrderAction(VirtualView view, int column, List<Integer> order) {
        super(view);
        this.column = column;
        this.order = order;
    }

    public int getColumn() {
        return column;
    }

    public List<Integer> getOrder() {
        return new ArrayList<>(order);
    }
}
