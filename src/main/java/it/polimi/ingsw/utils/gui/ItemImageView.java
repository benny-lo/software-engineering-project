package it.polimi.ingsw.utils.gui;

import javafx.scene.image.ImageView;

public class ItemImageView extends ImageView {
    private final int column;
    private final int row;
    /**
     * Creates a new view that represents an IMG element.
     * @param elem the element to create a view for
     * @param column column of the grid in which the elem is present
     *@param row row of the grid in which the elem is present
     */
    public ItemImageView(String elem, int column, int row) {
        super(elem);
        this.column = column;
        this.row = row;
    }

    public int getCoordinateX() {
        return column;
    }

    public int getCoordinateY() {
        return row;
    }
}
