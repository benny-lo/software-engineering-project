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
    @FXML
    private TextField usernameField;
    @FXML
    private Label welcomeText;
    @FXML
    private Button loginButton;

    private FXMLLoader fxmlLoader;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public static void startLoginController(GUInterface guInterface) {
        LoginController.guInterface = guInterface;
    }

    public void failedLogin() {
        welcomeText.setText("Login failed. Try again!");
    }
    public void invalidNickname() {
        welcomeText.setText("""
                This nickname is incorrect!
                It can only contains
                alphanumeric characters
                and underscores.""");
    }

    public void successfulLogin() {
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Lobby.fxml"));
            root = fxmlLoader.load();
            scene = new Scene(root);
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
