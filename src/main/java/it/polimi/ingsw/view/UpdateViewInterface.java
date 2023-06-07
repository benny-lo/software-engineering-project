package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.message.server.*;

public interface UpdateViewInterface {
    void onGamesList(GamesList message);
    void onGameData(GameData message);
    void onSelectedItems(SelectedItems message);
    void onAcceptedInsertion(AcceptedInsertion message);
    void onChatAccepted(ChatAccepted message);
    void onLivingRoomUpdate(LivingRoomUpdate update);
    void onBookshelfUpdate(BookshelfUpdate update);
    void onWaitingUpdate(WaitingUpdate update);
    void onScoresUpdate(ScoresUpdate update);
    void onEndingTokenUpdate(EndingTokenUpdate update);
    void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update);
    void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void onChatUpdate(ChatUpdate update);
    void onStartTurnUpdate(StartTurnUpdate update);
    void onEndGameUpdate(EndGameUpdate update);
}
