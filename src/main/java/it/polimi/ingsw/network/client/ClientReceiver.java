package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.networkMessage.server.*;

public interface ClientReceiver {
    void onGamesList(GamesList message);
    void onAcceptedAction(AcceptedAction message);
    void onItemsSelected(ItemsSelected message);
    void onLivingRoomUpdate(LivingRoomUpdate update);
    void onBookshelfUpdate(BookshelfUpdate update);
    void onWaitingUpdate(WaitingUpdate update);
    void onScoresUpdate(ScoresUpdate update);
    void onEndingTokenUpdate(EndingTokenUpdate update);
    void onCommonGoalCardUpdate(CommonGoalCardsUpdate update);
    void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void onChatUpdate(ChatUpdate update);
    void onStartTurnUpdate(StartTurnUpdate update);
    void onEndGameUpdate(EndGameUpdate update);
}
