package it.polimi.ingsw.utils.forTesting;

import it.polimi.ingsw.utils.networkMessage.server.*;

/**
 * This interface is exclusively for testing, and it mimics a ClientInterface.
 */

public interface ClientInterfaceTesting {
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
