package it.polimi.ingsw.utils.view.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * Class representing WindowManager.
 */
public class WindowManager {

    /**
     * Ask if the client wants to close the {@code Stage}, if the answer is positive then it closes the {@code Stage}.
     * @param stage The selected {@code Stage}.
     */
    public static void closeWindow(Stage stage) {
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
}
