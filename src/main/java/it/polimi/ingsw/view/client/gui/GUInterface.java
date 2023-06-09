package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.gui.controllers.*;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.ChatController.startChatController;
import static it.polimi.ingsw.view.client.gui.controllers.GameController.startGameController;
import static it.polimi.ingsw.view.client.gui.controllers.LobbyController.startLobbyController;
import static it.polimi.ingsw.view.client.gui.controllers.LoginController.*;
import static it.polimi.ingsw.view.client.gui.controllers.WaitingRoomController.startWaitingRoomController;

//TODO: when then server crashes, send an error
public class GUInterface extends ClientView {
    private LoginController loginController;
    private LobbyController lobbyController;
    private WaitingRoomController waitingRoomController;
    private GameController gameController;
    private ChatController chatController;
    private final List<String> nicknames = new ArrayList<>();

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
            String nickname = message.getNickname();
            if (isNicknameValid(nickname)) {
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
        clientConnection.send(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        clientConnection.send(message);
    }

    @Override
    public void writeChat(ChatMessage message) {
        clientConnection.send(message);
    }

    @Override
    public synchronized void onGamesList(GamesList message) {
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

        numberPlayers = message.getNumberPlayers();
        for (String player : message.getConnectedPlayers()) {
            Platform.runLater(() -> waitingRoomController.playerConnected(player));
            nicknames.add(player);
        }
        livingRoom = new Item[message.getLivingRoomRows()][message.getLivingRoomColumns()];
        numberCommonGoalCards = message.getNumberCommonGoalCards();
        bookshelvesColumns = message.getBookshelvesColumns();
        bookshelvesRows = message.getBookshelvesRows();
    }

    @Override
    public synchronized void onSelectedItems(SelectedItems message) {
        chosenItems = message.getItems();
        if (chosenItems == null){
            Platform.runLater(() -> gameController.resetOpacity());
            Platform.runLater(() -> gameController.clearSelectedItems());
            Platform.runLater(() -> gameController.failedSelection());
            return;
        } //TODO: invalid selection error
        System.out.println("lato server accettato");
        Platform.runLater(() -> gameController.clearTilesList());
        Platform.runLater(() -> gameController.setChosenItems(chosenItems));
    }

    @Override
    public synchronized void onAcceptedInsertion(AcceptedInsertion message) {
        if (!message.isAccepted()) {
            Platform.runLater(() -> gameController.failedInsertion());
            return;
        }
        Platform.runLater(() -> gameController.insertItems());
        Platform.runLater(() -> gameController.clearChosenItemsImageView());
    }

    @Override
    public void onChatAccepted(ChatAccepted message) {
        // TODO:
    }

    @Override
    public synchronized void onLivingRoomUpdate(LivingRoomUpdate update) {
        Map<Position, Item> ups = update.getLivingRoomUpdate();
        for (Position p : ups.keySet()) {
            livingRoom[p.getRow()][p.getColumn()] = ups.get(p);
        }
        Platform.runLater(() -> gameController.setLivingRoomGridPane(livingRoom));
    }

    @Override
    public synchronized void onBookshelfUpdate(BookshelfUpdate update) {
        Platform.runLater(() -> gameController.updateBookshelf(update.getOwner(), update.getBookshelf()));
    }

    @Override
    public synchronized void onWaitingUpdate(WaitingUpdate update) {
        if (update.isTypeOfAction()) {
            Platform.runLater(() -> waitingRoomController.playerConnected(update.getNickname()));
            nicknames.add(update.getNickname());
        } else {
            Platform.runLater(() -> waitingRoomController.playerDisconnected(update.getNickname()));
            nicknames.remove(update.getNickname());
        }

        if (update.getMissing() != 0) Platform.runLater(() -> waitingRoomController.waitingForPlayers(update.getMissing()));
        else {
            Platform.runLater(() -> waitingRoomController.startGame());
            Platform.runLater(() -> gameController.setNickname(nickname));
            Platform.runLater(() -> gameController.initializeBookshelves(nicknames, bookshelvesRows, bookshelvesColumns));
            Platform.runLater(() -> lobbyController.endWindow());
        }
    }

    @Override
    public void onScoresUpdate(ScoresUpdate update) {
        Platform.runLater(() -> gameController.setRankings(update.getScores()));
    }

    @Override
    public void onEndingTokenUpdate(EndingTokenUpdate update) {
        Platform.runLater(() -> gameController.setEndingToken(update.getOwner()));
    }

    @Override
    public synchronized void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        Platform.runLater(() -> gameController.updateCommonGoalCards(update.getCommonGoalCardsUpdate()));
    }

    @Override
    public synchronized void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        Platform.runLater(() -> gameController.setPersonalGoalCard(update.getId()));
    }

    @Override
    public void onChatUpdate(ChatUpdate update) {
        if (!gameController.isChatOpen()) {
            Platform.runLater(() -> {
                try {
                    gameController.enterChat();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        if (!gameController.isChatOpen()) {
            Platform.runLater(() -> chatController.receiveMessage(update));
        }
    }

    @Override
    public synchronized void onStartTurnUpdate(StartTurnUpdate update) {
        currentPlayer = update.getCurrentPlayer();
        Platform.runLater(() -> gameController.setCurrentPlayer(update.getCurrentPlayer()));
    }

    @Override
    public void onEndGameUpdate(EndGameUpdate update) {

    }

    @Override
    public void onDisconnection() {
//        if (disconnectionInLauncher()) return;
//        if (disconnectionInGame()) return;
        System.exit(0);
    } //TODO: implement this

    @Override
    public void start() {
        startGUI();
    }

    public synchronized String getNickname(){
        return nickname;
    }

    public synchronized List<String> getOthersNicknames() {
        return nicknames.stream().filter((n) -> n.equals(nickname)).toList();
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
