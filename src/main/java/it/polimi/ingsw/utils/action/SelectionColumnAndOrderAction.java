package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class SelectionColumnAndOrderAction extends Action {
    private final int column;
    private final List<Integer> order;

    /**
     * Constructor for the class.
     * @param view - view of the player selecting the column and order
     * @param column - column selected
     * @param order - order selected
     */
    public SelectionColumnAndOrderAction(VirtualView view, int column, List<Integer> order) {
        super(view);
        this.column = column;
        this.order = order;
    }

    /**
     * Getter for the column
     * @return - the column selected
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter for the order
     * @return - the order selected.
     */
    public List<Integer> getOrder() {
        return new ArrayList<>(order);
    }
}
