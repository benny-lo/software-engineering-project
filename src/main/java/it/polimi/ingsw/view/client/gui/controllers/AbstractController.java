package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUIViewInterface;

/**
 * Abstract class representing a Controller.
 */
public abstract class AbstractController {

    /**
     * Each controller has the {@code GUIViewInterface}.
     */
    protected static GUIViewInterface guInterface;

    /**
     * Sets the {@code GUIViewInterface}.
     * @param guInterface The {@code GUIViewInterface} reference.
     */
    public static void setGUInterfaceInControllers(GUIViewInterface guInterface) {
        AbstractController.guInterface = guInterface;
    }
}
