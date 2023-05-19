package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.networkMessage.server.*;

public interface ServerSender {
    void sendLivingRoomUpdate(LivingRoomUpdate update);
    void sendBookshelfUpdate(BookshelfUpdate update);
    void sendWaitingUpdate(WaitingUpdate update);
    void sendScoresUpdate(ScoresUpdate update);
    void sendEndingTokenUpdate(EndingTokenUpdate update);
    void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update);
    void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void sendChatUpdate(ChatUpdate update);
    void sendStartTurnUpdate(StartTurnUpdate update);
    void sendEndGameUpdate(EndGameUpdate update);
    void sendListOfGames(GamesList gamesList);
    void sendItemsSelected(ItemsSelected itemsSelected);
    void sendAcceptedAction(AcceptedAction acceptedAction);
}
