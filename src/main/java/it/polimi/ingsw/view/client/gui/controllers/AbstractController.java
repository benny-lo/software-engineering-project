package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUIViewInterface;

public abstract class AbstractController {
    protected static GUIViewInterface guInterface;

    public static void setGUInterfaceInControllers(GUIViewInterface guInterface) {
        AbstractController.guInterface = guInterface;
    }
}
