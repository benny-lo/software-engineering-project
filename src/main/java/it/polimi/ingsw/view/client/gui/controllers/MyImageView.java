package it.polimi.ingsw.view.client.gui.controllers;


import javafx.scene.image.ImageView;

public class MyImageView extends ImageView {
    private final int x;
    private final int y;
    private final String image;
    /**
     * Creates a new view that represents an IMG element.
     * @param elem the element to create a view for
     */
    public MyImageView(String elem, String image, int x, int y) {
        super(elem);
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public int getCoordinateX() {
        return x;
    }

    public int getCoordinateY() {
        return y;
    }

    public String getTile() {
        return image;
    }
}
