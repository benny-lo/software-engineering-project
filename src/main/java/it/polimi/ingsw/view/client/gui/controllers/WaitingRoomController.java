package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static it.polimi.ingsw.utils.gui.CloseWindow.logout;

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
            stage.show();
            stage.setResizable(false);
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/myShelfieImages/publisher_material/icon_50x50px.png"))));

            stage.setOnCloseRequest(event -> {event.consume();
                                                logout(stage);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
        playersListView.setCellFactory(param -> {
            TextFieldListCell<String> cell = new TextFieldListCell<>();
            cell.setFont(Font.font(22));
            return cell;
        });
    }
}
