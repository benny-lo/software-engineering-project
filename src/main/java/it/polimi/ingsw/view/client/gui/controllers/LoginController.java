package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.Nickname;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static it.polimi.ingsw.utils.view.gui.WindowManager.closeWindow;

/**
 * Class representing Login controller.
 * It manages the login and notifies the user if it is successful or not.
 */
public class LoginController extends AbstractController {
    private Stage stage;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    @FXML
    private TextField usernameField;

    /**
     * Sets {@code this} in the {@code GUInterface}.
     */
    @FXML
    public void initialize() {
        guInterface.setController(this);
    }

    /**
     * Notifies the user that the login failed, either the user is already connected or the nickname is already used.
     */
    public void failedLogin() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Login failed. Try again!");
        warningAlert.showAndWait();
    }

    /**
     * Notifies the user that the username is invalid.
     */
    public void invalidNickname() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("""
                This nickname is incorrect!
                It can only contains alphanumeric
                characters and underscores.""");
        warningAlert.showAndWait();
    }

    /**
     * Loads the Lobby {@code scene} and then shows it.
     */
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

    /**
     * Jump directly to {@code GameController}.
     */
    public void reconnection() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Game.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("MyShelfie");
            stage.show();
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/myShelfieImages/publisher_material/icon_50x50px.png"))));

            stage.setOnCloseRequest(event -> {event.consume();
                closeWindow(stage);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the username inserted by the client to the {@code GUInterface}.
     * @param event The {@code loginButton} has been clicked.
     * @throws IOException Error in I/O with the username field.
     */
    public void login(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        String username = usernameField.getText();

        guInterface.login(new Nickname(username));
    }

    /**
     * Notifies the client that has been disconnected from the server while being in the GUILauncher.
     */
    public void disconnectionInLauncher() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error!");
        alert.setContentText("You have been disconnected from the server.\n");
        alert.showAndWait();
        System.exit(0);
    }

    public void reconnectionInLauncher() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error!");
        alert.setContentText("You have been disconnected from the server.\n");
        alert.show();
    }
}
