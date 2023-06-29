package it.polimi.ingsw.view.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static it.polimi.ingsw.utils.view.gui.WindowManager.closeWindow;

/**
 * Class representing WaitingRoom controller.
 * It manages the list of connected players to the game.
 * If the {@code playersListView} is empty the game is destroyed.
 */
public class WaitingRoomController extends AbstractController {
    @FXML
    private Label waitingLabel;
    @FXML
    private ListView<String> playersListView;

    /**
     * Sets {@code this} in the {@code GUInterface}.
     * Sets the {code playersListView} cell size and font.
     */
    @FXML
    public void initialize() {
        guInterface.setController(this);
        playersListView.setCellFactory(param -> {
            TextFieldListCell<String> cell = new TextFieldListCell<>();
            cell.setFont(Font.font(22));
            return cell;
        });
    }

    /**
     * Adds a player to {@code playersListView}.
     * @param nickname The nickname of the player that has connected.
     */
    public void playerConnected(String nickname) {
        playersListView.getItems().add(nickname);
    }

    /**
     * Removes a player from {@code playersListView}.
     * @param nickname The nickname of the player that has disconnected.
     */
    public void playerDisconnected(String nickname) {
        playersListView.getItems().remove(nickname);
    }

    /**
     * Updates the {@code waitingLabel}.
     * @param missing The number of players that is needed to start the game.
     */
    public void waitingForPlayers(int missing) {
        if (missing == 1)
            waitingLabel.setText("Waiting for " + missing + " player");
        else 
            waitingLabel.setText("Waiting for " + missing + " players");
    }

    /**
     *  Creates the Game {@code stage}, loads the Game {@code scene}, and then shows it.
     */
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
                                                closeWindow(stage);});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
