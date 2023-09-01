package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;
import it.polimi.ingsw.view.client.gui.controllers.*;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.AbstractController.setGUInterfaceInControllers;

/**
 * Class representing the GUI.
 * It receives messages from the server and updates the controllers.
 * It sends the client's input to the server.
 */
public class GUInterface extends ClientView implements GUIViewInterface {
    private LoginController loginController;
    private LobbyController lobbyController;
    private WaitingRoomController waitingRoomController;
    private GameController gameController;
    private ChatController chatController;
    private final List<String> nicknames = new ArrayList<>();
    private boolean inLauncher = true;
    private boolean inGame = false;

    /**
     * {@inheritDoc}
     * It saves the player's nickname.
     * It synchronizes on {@code this}.
     * @param message Message containing the chosen nickname.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
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

        Platform.runLater(() -> {if (lobbyController != null) lobbyController.successfulCreateOrSelectGame(); });

        for (String player : message.getConnectedPlayers()) {
            Platform.runLater(() -> {if (waitingRoomController != null) waitingRoomController.playerConnected(player); });
            nicknames.add(player);
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
    @Override
    public synchronized void onSelectedItems(SelectedItems message) {
        List<Item> items = message.getItems();
        if (items == null){
            Platform.runLater(() -> gameController.resetOpacity());
            Platform.runLater(() -> gameController.clearSelectedItems());
            Platform.runLater(() -> gameController.failedSelection());
            return;
        }
        Platform.runLater(() -> gameController.clearTilesList());
        Platform.runLater(() -> gameController.setChosenItems(items));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
    @Override
    public synchronized void onAcceptedInsertion(AcceptedInsertion message) {
        if (!message.isAccepted()) {
            Platform.runLater(() -> gameController.failedInsertion());
            return;
        }
        Platform.runLater(() -> gameController.insertItems());
        Platform.runLater(() -> gameController.clearChosenItemsImageView());
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param message The message to process.
     */
    @Override
    public synchronized void onChatAccepted(ChatAccepted message) {
        if (message.isAccepted()) return;
        Platform.runLater(() -> chatController.rejectedMessage());
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onLivingRoomUpdate(LivingRoomUpdate update) {
        Platform.runLater(() -> gameController.setLivingRoomGridPane(update.getLivingRoomUpdate()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onBookshelfUpdate(BookshelfUpdate update) {
        Platform.runLater(() -> gameController.updateBookshelf(update.getOwner(), update.getBookshelf()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onScoresUpdate(ScoresUpdate update) {
        Platform.runLater(() -> gameController.setScores(update.getScores()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onEndingTokenUpdate(EndingTokenUpdate update) {
        Platform.runLater(() -> gameController.setEndingToken(update.getOwner()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        Platform.runLater(() -> gameController.updateCommonGoalCards(update.getCommonGoalCardsUpdate()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        Platform.runLater(() -> gameController.setPersonalGoalCard(update.getId()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onChatUpdate(ChatUpdate update) {
        Platform.runLater(() -> gameController.enterChat());
        Platform.runLater(() -> chatController.receiveMessage(update));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onStartTurnUpdate(StartTurnUpdate update) {
        Platform.runLater(() -> gameController.setCurrentPlayer(update.getCurrentPlayer()));
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param update The update to process.
     */
    @Override
    public synchronized void onEndGameUpdate(EndGameUpdate update) {
        if(update.getWinner() == null){
            Platform.runLater(() -> gameController.playerDisconnectionInGame());
        }else{
            Platform.runLater(() -> gameController.endGame(update.getWinner()));
        }
    }

    @Override
    public void onDisconnectionUpdate(Disconnection update) {
        String nick = update.getDisconnectedPlayer();

        if (nick != null) {
            Platform.runLater(() -> gameController.disconnectionInGame(nick));
            Platform.runLater(() -> chatController.disconnectedPlayer(nick));
            return;
        }

        if (inLauncher) {
            Platform.runLater(() -> loginController.disconnectionInLauncher());
        }
        if (inGame) {
            Platform.runLater(() -> gameController.disconnectionInGame());
        }
    }

    @Override
    public void onReconnectionUpdate(Reconnection update) {
        String nick = update.getReconnectedPlayer();

        if (nick.equals(nickname)) {
            Platform.runLater(() -> loginController.reconnectionInLauncher());
            Platform.runLater(() -> loginController.reconnection());

            inGame = true;
            inLauncher = false;

            Platform.runLater(() -> gameController.setNickname(nickname));
            Platform.runLater(() -> gameController.initializeBookshelves(nicknames));
        } else {
            Platform.runLater(() -> gameController.reconnectionInGame(nick));
            Platform.runLater(() -> chatController.reconnectedPlayer(nick));
        }
    }

    /**
     * Calls the {@code startGUI} method that starts the GUI.
     */
    @Override
    public void start() {
        setGUInterfaceInControllers(this);
        startGUI();
    }

    /**
     * {@inheritDoc}
     * @return The nickname of the client.
     */
    @Override
    public synchronized String getNickname(){
        return nickname;
    }

    /**
     * {@inheritDoc}
     * @return The nicknames of the other clients.
     */
    @Override
    public synchronized List<String> getOthersNicknames() {
        return nicknames.stream().filter((n) -> !n.equals(nickname)).toList();
    }

    /**
     * {@inheritDoc}
     * @param controller The controller reference.
     */
    @Override
    public synchronized void setController(LoginController controller) {
        loginController = controller;
    }

    /**
     * {@inheritDoc}
     * @param controller The controller reference.
     */
    @Override
    public synchronized void setController(LobbyController controller) {
        lobbyController = controller;
    }

    /**
     * {@inheritDoc}
     * @param controller The controller reference.
     */
    @Override
    public synchronized void setController(WaitingRoomController controller) {
        waitingRoomController =  controller;
    }

    /**
     * {@inheritDoc}
     * @param controller The controller reference.
     */
    @Override
    public synchronized void setController(GameController controller) {
        gameController = controller;
    }

    /**
     * {@inheritDoc}
     * @param controller The controller reference.
     */
    @Override
    public synchronized void setController(ChatController controller) {
        chatController = controller;
    }
}
