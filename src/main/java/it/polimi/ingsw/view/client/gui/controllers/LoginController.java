package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.Nickname;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static GUInterface guInterface;
    @FXML
    private TextField usernameField;
    @FXML
    private static Label welcomeText;
    @FXML
    private Button loginButton;


    public static void startLoginController(GUInterface guInterface) {
        LoginController.guInterface = guInterface;
    }

    public static void failedLogin() {
        //TODO: idk how to change this label
        //welcomeText.setText("failed login");
    }
    public static void invalidNickname() {
        //TODO: idk how to change this label
        //welcomeText.setText("invalid nickname");
    }

    public void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();

        guInterface.login(new Nickname(username));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //maybe we don't need this
    }
}
