package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUInterface;

public class LobbyController {
    private static GUInterface guInterface;

    public static void startLobbyController(GUInterface guInterface){
        LobbyController.guInterface = guInterface;
    }
}
