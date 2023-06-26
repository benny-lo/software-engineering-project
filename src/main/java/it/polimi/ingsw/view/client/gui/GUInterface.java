package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.gui.controllers.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.ChatController.startChatController;
import static it.polimi.ingsw.view.client.gui.controllers.GameController.startGameController;
import static it.polimi.ingsw.view.client.gui.controllers.LobbyController.startLobbyController;
import static it.polimi.ingsw.view.client.gui.controllers.LoginController.*;
import static it.polimi.ingsw.view.client.gui.controllers.WaitingRoomController.startWaitingRoomController;

public class GUInterface extends ClientView {
    private LoginController loginController;
    private LobbyController lobbyController;
    private WaitingRoomController waitingRoomController;
    private GameController gameController;
    private ChatController chatController;
    private final List<String> nicknames = new ArrayList<>();
    private boolean inLauncher = true;
    private boolean inGame = false;

    public GUInterface() {
        super();
        startLoginController(this);
        startLobbyController(this);
        startWaitingRoomController(this);
        startGameController(this);
        startChatController(this);
    }

    @Override
    public void login(Nickname message) {
        synchronized (this) {
            if (loginController == null) return;
            if (isNicknameValid(message.getNickname())) {
                Platform.runLater(() -> loginController.invalidNickname());
                return;
            }
            this.nickname = message.getNickname();
        }
        super.login(new Nickname(nickname));
    }

    @Override
    public synchronized void onGamesList(GamesList message) {
        if (loginController == null) return;
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
    public synchronized void onGameData(GameData message) {
        if (lobbyController == null) return;
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

        if (waitingRoomController == null) return;
        for (String player : message.getConnectedPlayers()) {
            Platform.runLater(() -> waitingRoomController.playerConnected(player));
            nicknames.add(player);
        }
    }

    @Override
    public synchronized void onSelectedItems(SelectedItems message) {
        if (gameController == null) return;
        if (message.getItems() == null){
            Platform.runLater(() -> gameController.resetOpacity());
            Platform.runLater(() -> gameController.clearSelectedItems());
            Platform.runLater(() -> gameController.failedSelection());
            return;
        }
        Platform.runLater(() -> gameController.clearTilesList());
        Platform.runLater(() -> gameController.setChosenItems(message.getItems()));
    }

    @Override
    public synchronized void onAcceptedInsertion(AcceptedInsertion message) {
        if (gameController == null) return;
        if (!message.isAccepted()) {
            Platform.runLater(() -> gameController.failedInsertion());
            return;
        }
        Platform.runLater(() -> gameController.insertItems());
        Platform.runLater(() -> gameController.clearChosenItemsImageView());
    }

    @Override
    public synchronized void onChatAccepted(ChatAccepted message) {
        if (chatController == null) return;
        if (message.isAccepted()) return;
        Platform.runLater(() -> chatController.rejectedMessage());
    }

    @Override
    public synchronized void onLivingRoomUpdate(LivingRoomUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setLivingRoomGridPane(update.getLivingRoomUpdate()));
    }

    @Override
    public synchronized void onBookshelfUpdate(BookshelfUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.updateBookshelf(update.getOwner(), update.getBookshelf()));
    }

    @Override
    public synchronized void onWaitingUpdate(WaitingUpdate update) {
        if (waitingRoomController == null) return;
        if (update.isConnected()) {
            Platform.runLater(() -> waitingRoomController.playerConnected(update.getNickname()));
            nicknames.add(update.getNickname());
        } else {
            Platform.runLater(() -> waitingRoomController.playerDisconnected(update.getNickname()));
            nicknames.remove(update.getNickname());
        }

        if (update.getMissing() != 0) Platform.runLater(() -> waitingRoomController.waitingForPlayers(update.getMissing()));
        else {
            Platform.runLater(() -> waitingRoomController.startGame());
            inGame = true;
            inLauncher = false;
            if (gameController == null || lobbyController == null) return;
            Platform.runLater(() -> gameController.setNickname(nickname));
            Platform.runLater(() -> gameController.initializeBookshelves(nicknames));
            Platform.runLater(() -> lobbyController.endWindow());
        }
    }

    @Override
    public synchronized void onScoresUpdate(ScoresUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setScores(update.getScores()));
    }

    @Override
    public synchronized void onEndingTokenUpdate(EndingTokenUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setEndingToken(update.getOwner()));
    }

    @Override
    public synchronized void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.updateCommonGoalCards(update.getCommonGoalCardsUpdate()));
    }

    @Override
    public synchronized void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setPersonalGoalCard(update.getId()));
    }

    @Override
    public synchronized void onChatUpdate(ChatUpdate update) {
        if (gameController == null || chatController == null) return;
        Platform.runLater(() -> gameController.enterChat());
        Platform.runLater(() -> chatController.receiveMessage(update));
    }

    @Override
    public synchronized void onStartTurnUpdate(StartTurnUpdate update) {
        if (gameController == null) return;
        Platform.runLater(() -> gameController.setCurrentPlayer(update.getCurrentPlayer()));
    }

    @Override
    public synchronized void onEndGameUpdate(EndGameUpdate update) {
        if (gameController == null) return;
        if(update.getWinner() == null){
            Platform.runLater(() -> gameController.playerDisconnectionInGame());
        }else{
            Platform.runLater(() -> gameController.endGame(update.getWinner()));
        }
    }

    @Override
    public synchronized void onDisconnection() {
        if (inLauncher) {
            if (loginController == null) return;
            Platform.runLater(() -> loginController.disconnectionInLauncher());
        }
        if (inGame) {
            if (gameController == null) return;
            Platform.runLater(() -> gameController.disconnectionInGame());
        }
    }

    @Override
    public void start() {
        startGUI();
    }

    public synchronized String getNickname(){
        return nickname;
    }

    public synchronized List<String> getOthersNicknames() {
        return nicknames.stream().filter((n) -> !n.equals(nickname)).toList();
    }

    public synchronized void receiveController(LoginController controller){
        loginController = controller;
    }
    public synchronized void receiveController(LobbyController controller){
        lobbyController = controller;
    }
    public synchronized void receiveController(WaitingRoomController controller){
        waitingRoomController = controller;
    }
    public synchronized void receiveController(GameController controller){
        gameController = controller;
    }
    public synchronized void receiveController(ChatController controller){
        chatController = controller;
    }
}
