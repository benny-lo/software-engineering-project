package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    private static GUInterface guInterface;
    @FXML
    private VBox chatVBox;
    @FXML
    private Button sendButton;
    @FXML
    private Label sendLabel;


    public static void startChatController(GUInterface guInterface){
        ChatController.guInterface = guInterface;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
    }
}
