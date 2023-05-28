package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.gui.controllers.LobbyController;
import it.polimi.ingsw.view.client.gui.controllers.LoginController;
import it.polimi.ingsw.view.client.gui.controllers.WaitingRoomController;
import javafx.application.Platform;

import java.util.List;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.LobbyController.startLobbyController;
import static it.polimi.ingsw.view.client.gui.controllers.LoginController.*;
import static it.polimi.ingsw.view.client.gui.controllers.WaitingRoomController.startWaitingRoomController;

//TODO: implement everything
public class GUInterface extends ClientView {
    private LoginController loginController;
    private LobbyController lobbyController;
    private WaitingRoomController waitingRoomController;

    public GUInterface() {
        super();
        startLoginController(this);
        startLobbyController(this);
        startWaitingRoomController(this);
    }

    @Override
    public void login(Nickname message) {
        synchronized (this) {
            String nickname = message.getNickname();
            if (!isValidNickname(nickname)) {
                Platform.runLater(() -> loginController.invalidNickname());
                return;
            }
            this.nickname = nickname;
        }
        clientConnection.send(new Nickname(nickname));
    }

    @Override
    public void createGame(GameInitialization message) {
        clientConnection.send(message);
    }

    @Override
    public void selectGame(GameSelection message) {
        clientConnection.send(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {

    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {

    }

    @Override
    public void writeChat(ChatMessage message) {

    }

    @Override
    public void onGamesList(GamesList message) {
        List<GameInfo> games = message.getAvailable();

        if (games == null) {
            Platform.runLater(() -> loginController.failedLogin());
            nickname = null;
            return;
        }

        Platform.runLater(() -> loginController.successfulLogin());

        Platform.runLater(() -> lobbyController.listOfGames(games));
    }

    @Override
    public void onGameData(GameData message) {
        if (message.getNumberPlayers() == -1 ||
                message.getNumberCommonGoalCards() == -1 ||
                message.getBookshelvesColumns() == -1 ||
                message.getBookshelvesRows() == -1 ||
                message.getLivingRoomColumns() == -1 ||
                message.getLivingRoomRows() == -1) {
            Platform.runLater(() -> lobbyController.failedCreateGame());
            return;
        }

        Platform.runLater(() -> lobbyController.successfulCreateOrSelectGame());

        //maybe we don't need of all these attributes
        numberPlayers = message.getNumberPlayers();
        numberCommonGoalCards = message.getNumberCommonGoalCards();
        livingRoom = new Item[message.getLivingRoomRows()][message.getLivingRoomColumns()];
        bookshelvesRows = message.getBookshelvesRows();
        bookshelvesColumns = message.getBookshelvesColumns();
        personalGoalCard = new Item[bookshelvesRows][bookshelvesColumns];
    }

    @Override
    public void onItemsSelected(ItemsSelected message) {

    }

    @Override
    public void onAcceptedInsertion(AcceptedInsertion message) {

    }

    @Override
    public void onChatAccepted(ChatAccepted message) {

    }

    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {

    }

    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {

    }

    @Override
    public void onWaitingUpdate(WaitingUpdate update) {
        if (update.isTypeOfAction()) Platform.runLater(() -> waitingRoomController.playerConnected(update.getNickname()));
        else Platform.runLater(() -> waitingRoomController.playerDisconnected(update.getNickname()));

        if (update.getMissing() != 0) Platform.runLater(() -> waitingRoomController.waitingForPlayers(update.getMissing()));
        else {
            Platform.runLater(() -> waitingRoomController.startGame());
            Platform.runLater(() -> lobbyController.endWindow());
        }
    }

    @Override
    public void onScoresUpdate(ScoresUpdate update) {

    }

    @Override
    public void onEndingTokenUpdate(EndingTokenUpdate update) {

    }

    @Override
    public void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {

    }

    @Override
    public void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {

    }

    @Override
    public void onChatUpdate(ChatUpdate update) {

    }

    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {

    }

    @Override
    public void onEndGameUpdate(EndGameUpdate update) {

    }

    @Override
    public void onDisconnection() {
        System.exit(0);
    }

    @Override
    public void start() {
        startGUI();
    }

    public String getNickname(){
        return nickname;
    }

    public void receiveController(LoginController controller){
        loginController = controller;
    }

    public void receiveController(LobbyController controller){
        lobbyController = controller;
    }

    public void receiveController(WaitingRoomController controller){
        waitingRoomController = controller;
    }
}
