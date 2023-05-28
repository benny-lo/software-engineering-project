package it.polimi.ingsw.view.client.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUILauncher extends Application {
    private static FXMLLoader fxmlLoader;
    public static void startGUI(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyShelfie");
        primaryStage.setWidth(1920);
        primaryStage.setHeight(1080);
        primaryStage.show();
        primaryStage.setMaximized(false);
    }
}
