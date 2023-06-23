package it.polimi.ingsw.view.client.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

import static it.polimi.ingsw.utils.gui.WindowManager.closeWindow;


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
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/myShelfieImages/publisher_material/icon_50x50px.png"))));

        primaryStage.setOnCloseRequest(event -> {event.consume();
                                                closeWindow(primaryStage);});
    }

}
