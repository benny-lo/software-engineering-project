package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.GameInitialization;
import it.polimi.ingsw.utils.message.client.GameSelection;
import it.polimi.ingsw.utils.message.server.GameInfo;
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

/**
 * Class representing Lobby controller.
 * It manages the creation of a new game or the selection of an already existing game by the client.
 */
public class LobbyController extends AbstractController {
    private static final int MIN_NUMBER_PLAYERS = 2;
    private static final int MAX_NUMBER_PLAYERS = 4;
    private static final int DEFAULT_NUMBER_PLAYERS = 3;
    private static final int MIN_NUMBER_COMMON_GOAL_CARDS = 1;
    private static final int MAX_NUMBER_COMMON_GOAL_CARDS = 2;
    private static final int DEFAULT_NUMBER_COMMON_GOAL_CARDS = 2;
    private Stage stage;
    private GameInfo currentSelectedGame;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    @FXML
    private Spinner<Integer> numberPlayersSpinner;
    @FXML
    private Spinner<Integer> numberCommonGoalCardsSpinner;
    @FXML
    private ListView<GameInfo> gamesListView;
    @FXML
    private Label displayNicknameLabel;

    /**
     * Sets {@code this} in the {@code GUInterface}.
     * Creates and sets the spinners.
     * Sets the client nickname.
     * Sets the listener to the {@code gameListView}.
     */
    @FXML
    public void initialize() {
        guInterface.setController(this);

        displayNicknameLabel.setText("Hi " + guInterface.getNickname() + "!");

        SpinnerValueFactory<Integer> numberPlayersValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_NUMBER_PLAYERS, MAX_NUMBER_PLAYERS);
        numberPlayersValueFactory.setValue(DEFAULT_NUMBER_PLAYERS);
        numberPlayersSpinner.setValueFactory(numberPlayersValueFactory);

        SpinnerValueFactory<Integer> numberCommonGoalCardsValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_NUMBER_COMMON_GOAL_CARDS, MAX_NUMBER_COMMON_GOAL_CARDS);
        numberCommonGoalCardsValueFactory.setValue(DEFAULT_NUMBER_COMMON_GOAL_CARDS);
        numberCommonGoalCardsSpinner.setValueFactory(numberCommonGoalCardsValueFactory);

        gamesListView.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> currentSelectedGame = gamesListView.getSelectionModel().getSelectedItem());
    }

    /**
     * Sends the selected game attributes to the {@code GUInterface}.
     * @param event The {@code createGameButton} has been clicked.
     * @throws IOException Error with I/O.
     */
    public void createGame(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        guInterface.createGame(new GameInitialization(numberPlayersSpinner.getValue(), numberCommonGoalCardsSpinner.getValue()));
    }

    /**
     * Sends the selected game to the {@code GUInterface}.
     * Notifies the client if the selection gone wrong.
     * @param event The {@code selectGameButton} has been clicked.
     * @throws IOException Error with I/O.
     */
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

    /**
     * Updates the list of games showed by the GUI.
     * @param games {@code List} of games received by the {@code GUInterface}.
     */
    public void listOfGames(List<GameInfo> games) {
        for (GameInfo game : games) {
            gamesListView.getItems().removeIf(g -> g.getId() == game.getId());
            if (game.getNumberPlayers() != -1 && game.getNumberCommonGoals() != -1) {
                gamesListView.getItems().add(game);
            }
        }
    }

    /**
     * Notifies the client that the game creation gone wrong.
     */
    public void failedCreateGame() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("""
            Something gone wrong in the game creation!
            Please retry.""");
        warningAlert.showAndWait();
    }

    /**
     * Loads the WaitingRoom {@code scene} and then shows it.
     */
    public void successfulCreateOrSelectGame() {
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

    /**
     * Closes the Launcher window when the game starts.
     */
    public void endWindow() {
        stage.close();
    }
}
