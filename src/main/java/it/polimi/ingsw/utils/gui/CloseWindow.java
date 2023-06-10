package it.polimi.ingsw.utils.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

//TODO: find a better name for this class

public class CloseWindow {
    public static void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you really want to quit?");

        Optional<ButtonType> opt = alert.showAndWait();
        if (opt.isPresent() && opt.get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }

    public static void exitChat(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Chat");
        alert.setHeaderText("You're about to leave the chat!");
        alert.setContentText("Do you really want to exit?");

        Optional<ButtonType> opt = alert.showAndWait();
        if (opt.isPresent() && opt.get() == ButtonType.OK) {
            stage.hide();
        }
    }
}
