package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.Nickname;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private static GUInterface guInterface;
    public Button loginButton;
    @FXML
    private TextField usernameField;
    @FXML
    private Label welcomeText;
    @FXML
    private Label errorLabel;

    private Stage stage;


    public static void startLoginController(GUInterface guInterface) {
        LoginController.guInterface = guInterface;
    }

    public void failedLogin() {
        errorLabel.setText("Login failed. Try again!");
    }
    public void invalidNickname() {
        errorLabel.setText("""
                This nickname is incorrect! It can only contains
                alphanumeric characters and underscores.""");
    }

    public void successfulLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Lobby.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();


        String username = usernameField.getText();

        guInterface.login(new Nickname(username));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this); // IDK if it is supposed to work, but it works
    }
}
