package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.gui.controllers.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.AbstractController.setGUInterfaceInControllers;

public class GUInterface extends ClientView implements GUIViewInterface {
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
        setGUInterfaceInControllers(this);
    }

    @Override
    public void login(Nickname message) {
        synchronized (this) {
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

        for (String player : message.getConnectedPlayers()) {
            Platform.runLater(() -> waitingRoomController.playerConnected(player));
            nicknames.add(player);
        }
    }

    @Override
    public synchronized void onSelectedItems(SelectedItems message) {
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
        if (!message.isAccepted()) {
            Platform.runLater(() -> gameController.failedInsertion());
            return;
        }
        Platform.runLater(() -> gameController.insertItems());
        Platform.runLater(() -> gameController.clearChosenItemsImageView());
    }

    @Override
    public synchronized void onChatAccepted(ChatAccepted message) {
        if (message.isAccepted()) return;
        Platform.runLater(() -> chatController.rejectedMessage());
    }

    @Override
    public synchronized void onLivingRoomUpdate(LivingRoomUpdate update) {
        Platform.runLater(() -> gameController.setLivingRoomGridPane(update.getLivingRoomUpdate()));
    }

    @Override
    public synchronized void onBookshelfUpdate(BookshelfUpdate update) {
        Platform.runLater(() -> gameController.updateBookshelf(update.getOwner(), update.getBookshelf()));
    }

    @Override
    public synchronized void onWaitingUpdate(WaitingUpdate update) {
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
            Platform.runLater(() -> gameController.setNickname(nickname));
            Platform.runLater(() -> gameController.initializeBookshelves(nicknames));
            Platform.runLater(() -> lobbyController.endWindow());
        }
    }

    @Override
    public synchronized void onScoresUpdate(ScoresUpdate update) {
        Platform.runLater(() -> gameController.setScores(update.getScores()));
    }

    @Override
    public synchronized void onEndingTokenUpdate(EndingTokenUpdate update) {
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
    public synchronized void onChatUpdate(ChatUpdate update) {
        Platform.runLater(() -> gameController.enterChat());
        Platform.runLater(() -> chatController.receiveMessage(update));
    }

    @Override
    public synchronized void onStartTurnUpdate(StartTurnUpdate update) {
        Platform.runLater(() -> gameController.setCurrentPlayer(update.getCurrentPlayer()));
    }

    @Override
    public synchronized void onEndGameUpdate(EndGameUpdate update) {
        if(update.getWinner() == null){
            Platform.runLater(() -> gameController.playerDisconnectionInGame());
        }else{
            Platform.runLater(() -> gameController.endGame(update.getWinner()));
        }
    }

    @Override
    public synchronized void onDisconnection() {
        if (inLauncher) {
            Platform.runLater(() -> loginController.disconnectionInLauncher());
        }
        if (inGame) {
            Platform.runLater(() -> gameController.disconnectionInGame());
        }
    }

    @Override
    public void start() {
        startGUI();
    }

    @Override
    public synchronized String getNickname(){
        return nickname;
    }

    @Override
    public synchronized List<String> getOthersNicknames() {
        return nicknames.stream().filter((n) -> !n.equals(nickname)).toList();
    }

    @Override
    public synchronized void receiveController(AbstractController controller) {
        if (controller instanceof LoginController)
            loginController = (LoginController) controller;
        else if (controller instanceof  LobbyController)
            lobbyController = (LobbyController) controller;
        else if (controller instanceof WaitingRoomController)
            waitingRoomController = (WaitingRoomController) controller;
        else if (controller instanceof GameController)
            gameController = (GameController) controller;
        else if (controller instanceof ChatController)
            chatController = (ChatController) controller;
    }
}
