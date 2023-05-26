package it.polimi.ingsw.view.client.gui;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIInterface extends Application {

    public static void startGUI(){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MyShelfie");
        primaryStage.setWidth(1080);
        primaryStage.setHeight(720);
        primaryStage.show();
        primaryStage.setMaximized(false);
    }
}
