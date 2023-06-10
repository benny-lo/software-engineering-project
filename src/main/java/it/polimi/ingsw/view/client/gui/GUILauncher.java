package it.polimi.ingsw.view.client.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static it.polimi.ingsw.utils.gui.CloseWindow.logout;


public class GUILauncher extends Application{
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
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {event.consume();
                                                logout(primaryStage);});
    }

}
