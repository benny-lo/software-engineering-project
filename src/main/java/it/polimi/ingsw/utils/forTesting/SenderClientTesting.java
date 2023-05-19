package it.polimi.ingsw.utils.forTesting;

import it.polimi.ingsw.network.server.ServerSender;
import it.polimi.ingsw.utils.networkMessage.server.*;

/**
 * This class is exclusively for testing, and it mimics an UpdateSender class.
 */

public class SenderClientTesting implements ServerSender {
    private final ClientInterfaceTesting client;

    public SenderClientTesting(ClientInterfaceTesting client) {
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
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) {
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

    @Override
    public void sendListOfGames(GamesList gamesList) {
        client.sendListOfGames(gamesList);
    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {
        client.sendItemsSelected(itemsSelected);
    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {

    }
}
