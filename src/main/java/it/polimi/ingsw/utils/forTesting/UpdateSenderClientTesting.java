package it.polimi.ingsw.utils.forTesting;

import it.polimi.ingsw.network.server.UpdateSender;
import it.polimi.ingsw.utils.networkMessage.server.*;

/**
 * This class is exclusively for testing, and it mimics an UpdateSender class.
 */

public class UpdateSenderClientTesting implements UpdateSender {
    private final ClientInterfaceTesting client;

    public UpdateSenderClientTesting(ClientInterfaceTesting client) {
        this.client = client;
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        client.sendLivingRoomUpdate(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        client.sendBookshelfUpdate(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        client.sendWaitingUpdate(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        client.sendScoresUpdate(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        client.sendEndingTokenUpdate(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) {
        client.sendCommonGoalCardUpdate(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        client.sendPersonalGoalCardUpdate(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        client.sendChatUpdate(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        client.sendStartTurnUpdate(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        client.sendEndGameUpdate(update);
    }
}
