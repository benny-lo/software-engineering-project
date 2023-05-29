package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static it.polimi.ingsw.view.client.gui.GUILauncher.logout;

//TODO: the view list doesn't receive the players that had already entered the game
public class WaitingRoomController implements Initializable {
    private static GUInterface guInterface;
    @FXML
    private Label waitingLabel;
    @FXML
    private ListView<String> playersListView;

    public static void startWaitingRoomController(GUInterface guInterface) {
        WaitingRoomController.guInterface = guInterface;
    }

    public void playerConnected(String nickname) {
        playersListView.getItems().add(nickname);
    }
    
    public void playerDisconnected(String nickname) {
        playersListView.getItems().remove(nickname);
    }
    
    public void waitingForPlayers(int missing) {
        if (missing == 1)
            waitingLabel.setText("Waiting for " + missing + " player");
        else 
            waitingLabel.setText("Waiting for " + missing + " players");
    }
    
    public void startGame(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Game.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("MyShelfie");
            stage.setWidth(1080);
            stage.setHeight(720);
            stage.show();

            stage.setOnCloseRequest(event -> {event.consume();
                                                logout(stage);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this); // IDK if it is supposed to work, but it works
    }
}
