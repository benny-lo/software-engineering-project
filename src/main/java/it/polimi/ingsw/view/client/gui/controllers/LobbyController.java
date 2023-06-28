package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.GameInitialization;
import it.polimi.ingsw.utils.message.client.GameSelection;
import it.polimi.ingsw.utils.message.server.GameInfo;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class LobbyController {
    private static GUInterface guInterface;
    private Stage stage;
    private GameInfo currentSelectedGame;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    private final static int minNumberPlayers = 2;
    private final static int maxNumberPlayers = 4;
    private final static int defaultNumberPlayers = 3;
    private final static int minNumberCommonGoalCards = 1;
    private final static int maxNumberCommonGoalCards = 2;
    private final static int defaultNumberCommonGoalCards = 2;
    @FXML
    private Spinner<Integer> numberPlayersSpinner;
    @FXML
    private Spinner<Integer> numberCommonGoalCardsSpinner;
    @FXML
    private ListView<GameInfo> gamesListView;
    @FXML
    private Label displayNicknameLabel;

    public static void startLobbyController(GUInterface guInterface){
        LobbyController.guInterface = guInterface;
    }

    public void createGame(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        guInterface.createGame(new GameInitialization(numberPlayersSpinner.getValue(), numberCommonGoalCardsSpinner.getValue()));
    }

    public void selectGame(ActionEvent event) throws IOException {
        if (currentSelectedGame == null){

            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("""
                Something gone wrong in the game selection!
                Please retry.""");
            warningAlert.showAndWait();
            return;
        }
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        guInterface.selectGame(new GameSelection(currentSelectedGame.getId()));
    }

    public void listOfGames(List<GameInfo> games){
        for (GameInfo game : games) {
            List<GameInfo> other = gamesListView.getItems().stream().filter((g) -> g.getId() != game.getId()).toList();
            gamesListView.getItems().removeAll(other);
            if (game.getNumberPlayers() != -1 && game.getNumberCommonGoals() != -1) {
                gamesListView.getItems().add(game);
            }
        }
    }

    public void failedCreateGame() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("""
            Something gone wrong in the game creation!
            Please retry.""");
        warningAlert.showAndWait();
    }

    public void successfulCreateOrSelectGame(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/WaitingRoom.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void endWindow(){
        stage.close();
    }

    @FXML
    public void initialize() {
        guInterface.receiveController(this);

        displayNicknameLabel.setText("Hi " + guInterface.getNickname() + "!");

        SpinnerValueFactory<Integer> numberPlayersValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minNumberPlayers, maxNumberPlayers);
        numberPlayersValueFactory.setValue(defaultNumberPlayers);
        numberPlayersSpinner.setValueFactory(numberPlayersValueFactory);

        SpinnerValueFactory<Integer> numberCommonGoalCardsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(minNumberCommonGoalCards, maxNumberCommonGoalCards);
        numberCommonGoalCardsValueFactory.setValue(defaultNumberCommonGoalCards);
        numberCommonGoalCardsSpinner.setValueFactory(numberCommonGoalCardsValueFactory);

        gamesListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> currentSelectedGame = gamesListView.getSelectionModel().getSelectedItem());
    }
}
