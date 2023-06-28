package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.Nickname;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController extends AbstractController {
    private Stage stage;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    @FXML
    public Button loginButton;
    @FXML
    private TextField usernameField;

    public void failedLogin() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Login failed. Try again!");
        warningAlert.showAndWait();
    }
    public void invalidNickname() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("""
                This nickname is incorrect!
                It can only contains alphanumeric
                characters and underscores.""");
        warningAlert.showAndWait();
    }

    public void successfulLogin() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Lobby.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        String username = usernameField.getText();

        guInterface.login(new Nickname(username));
    }

    public void disconnectionInLauncher() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error!");
        alert.setContentText("You have been disconnected from the server.\n");
        alert.showAndWait();
        System.exit(0);
    }
    @FXML
    public void initialize() {
        guInterface.receiveController(this);
    }
}
