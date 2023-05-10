package it.polimi.ingsw.utils.forTesting;

import it.polimi.ingsw.utils.networkMessage.server.*;

import java.io.Serializable;

/**
 * This class is exclusively for testing, and it mimics a Client class.
 */

public class ClientTesting implements ClientInterfaceTesting, Serializable {
    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update){

    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update){

    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update){

    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update){

    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update){

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update){

    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update){

    }

    @Override
    public void sendChatUpdate(ChatUpdate update){

    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update){

    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update){

    }
}