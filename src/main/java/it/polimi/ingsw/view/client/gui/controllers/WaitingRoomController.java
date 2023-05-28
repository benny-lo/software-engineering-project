package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

//TODO: implement waiting update and startGame method
public class WaitingRoomController implements Initializable {
    private static GUInterface guInterface;

    public static void startWaitingRoomController(GUInterface guInterface){
        WaitingRoomController.guInterface = guInterface;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this); // IDK if it is supposed to work, but it works
    }
}
