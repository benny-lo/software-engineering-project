package it.polimi.ingsw.utils.classesOnlyForTesting;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.VirtualView;

public class MockVirtualView extends VirtualView {
    public MockVirtualView() {
    }

    @Override
    public void setNickname(String nickname) {
        super.setNickname(nickname);
    }

    @Override
    public String getNickname() {
        return super.getNickname();
    }

    @Override
    public void setController(Controller controller) {
        super.setController(controller);
    }

    @Override
    public boolean isLoggedIn() {
        return super.isLoggedIn();
    }

    @Override
    public boolean isInGame() {
        return super.isInGame();
    }

    @Override
    public void login(Nickname message) {
        super.login(message);
    }

    @Override
    public void createGame(GameInitialization message) {
        super.createGame(message);
    }

    @Override
    public void selectGame(GameSelection message) {
        super.selectGame(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        super.selectFromLivingRoom(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        super.insertInBookshelf(message);
    }

    @Override
    public void writeChat(ChatMessage message) {
        super.writeChat(message);
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
    public void onGamesList(GamesList gamesList) {
    }

    @Override
    public void onItemsSelected(ItemsSelected itemsSelected) {
    }

    @Override
    public void onAcceptedAction(AcceptedAction acceptedAction) {
    }
}
