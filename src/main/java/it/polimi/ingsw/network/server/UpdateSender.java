package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.networkMessage.server.*;

public interface UpdateSender {
    void sendLivingRoomUpdate(LivingRoomUpdate update);
    void sendBookshelfUpdate(BookshelfUpdate update);
    void sendWaitingUpdate(WaitingUpdate update);
    void sendScoresUpdate(ScoresUpdate update);
    void sendEndingTokenUpdate(EndingTokenUpdate update);
    void sendCommonGoalCardUpdate(CommonGoalCardUpdate update);
    void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void sendChatUpdate(ChatUpdate update);
    void sendStartTurnUpdate(StartTurnUpdate update);
    void sendEndGameUpdate(EndGameUpdate update);
}
