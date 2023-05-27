package it.polimi.ingsw.view.client.gui;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.client.ClientView;

import static it.polimi.ingsw.view.client.gui.GUILauncher.startGUI;
import static it.polimi.ingsw.view.client.gui.controllers.LobbyController.startLobbyController;
import static it.polimi.ingsw.view.client.gui.controllers.LoginController.*;

//TODO: implement everything
public class GUInterface extends ClientView {

    public GUInterface() {
        super();
        startLoginController(this);
        startLobbyController(this);
    }

    @Override
    public void login(Nickname message) {
        synchronized (this) {
            String nickname = message.getNickname();
            if (!isValidNickname(nickname)) {
                invalidNickname();
                return;
            }
            this.nickname = nickname;
        }
        clientConnection.send(new Nickname(nickname));
    }

    @Override
    public void createGame(GameInitialization message) {

    }

    @Override
    public void selectGame(GameSelection message) {

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

    }

    @Override
    public void onGameData(GameData message) {
        if (message.getNumberPlayers() == -1 ||
                message.getNumberCommonGoalCards() == -1 ||
                message.getBookshelvesColumns() == -1 ||
                message.getBookshelvesRows() == -1 ||
                message.getLivingRoomColumns() == -1 ||
                message.getLivingRoomRows() == -1) {
            failedLogin();
            return;
        }

        //successfulLogin();
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

    }

    @Override
    public void start() {
        startGUI();
    }
}
