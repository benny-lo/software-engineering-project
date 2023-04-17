package it.polimi.ingsw.utils.action;

import java.util.ArrayList;
import java.util.List;

public class SelectionColumnAndOrderAction extends Action {
    private final int column;
    private final List<Integer> order;

    public SelectionColumnAndOrderAction(String nickname, int column, List<Integer> order) {
        super(nickname);
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
