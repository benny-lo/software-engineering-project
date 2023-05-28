package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.GameInitialization;
import it.polimi.ingsw.utils.message.client.GameSelection;
import it.polimi.ingsw.utils.message.server.GameInfo;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {
    private static GUInterface guInterface;
    @FXML
    private Spinner<Integer> numberPlayersSpinner;
    @FXML
    private Spinner<Integer> numberCommonGoalCardsSpinner;
    @FXML
    private Button createGameButton;
    @FXML
    private Button selectGameButton;
    @FXML
    private Label errorCreateGameLabel;
    @FXML
    private Label errorSelectGameLabel;
    @FXML
    private ListView<GameInfo> gamesListView;
    @FXML
    private Label displayNicknameLabel;

    private Stage stage;
    private GameInfo currentSelectedGame;

    public static void startLobbyController(GUInterface guInterface){
        LobbyController.guInterface = guInterface;
    }

    public void createGame(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        guInterface.createGame(new GameInitialization(numberPlayersSpinner.getValue(), numberCommonGoalCardsSpinner.getValue()));
    }

    public void selectGame(ActionEvent event) throws IOException {
        if (currentSelectedGame == null){
            errorSelectGameLabel.setVisible(true);
            return;
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        guInterface.selectGame(new GameSelection(currentSelectedGame.getId()));
    }

    public void listOfGames(List<GameInfo> games){
        gamesListView.getItems().addAll(games);
    }

    public void failedCreateGame(){
        errorCreateGameLabel.setVisible(true);
    }

    public void successfulCreateOrSelectGame(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/WaitingRoom.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endWindow(){
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this); // IDK if it is supposed to work, but it works

        displayNicknameLabel.setText("Hi " + guInterface.getNickname() + "!");

        SpinnerValueFactory<Integer> numberPlayersValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(2,4);
        numberPlayersValueFactory.setValue(3);
        numberPlayersSpinner.setValueFactory(numberPlayersValueFactory);

        SpinnerValueFactory<Integer> numberCommonGoalCardsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,2);
        numberCommonGoalCardsValueFactory.setValue(2);
        numberCommonGoalCardsSpinner.setValueFactory(numberCommonGoalCardsValueFactory);

        gamesListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> currentSelectedGame = gamesListView.getSelectionModel().getSelectedItem());
    }
}
