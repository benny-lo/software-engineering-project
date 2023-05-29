package it.polimi.ingsw.view.client.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class GUILauncher extends Application implements Initializable {
    public static void startGUI(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyShelfieLauncher");
        primaryStage.setWidth(1080);
        primaryStage.setHeight(720);
        primaryStage.show();
        primaryStage.setMaximized(false);

        primaryStage.setOnCloseRequest(event -> {event.consume();
                                                logout(primaryStage);});
    }

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
