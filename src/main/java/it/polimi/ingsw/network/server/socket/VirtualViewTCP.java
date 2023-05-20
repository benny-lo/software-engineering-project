package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.utils.networkMessage.server.*;

public class VirtualViewTCP extends VirtualView {
    private final ServerConnectionTCP serverConnection;
    public VirtualViewTCP(Lobby lobby, ServerConnectionTCP serverConnection) {
        super(lobby);
        this.serverConnection = serverConnection;
    }
    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        serverConnection.sendAsync(update);
    }

    @Override
    public void sendListOfGames(GamesList gamesList) {
        serverConnection.sendAsync(gamesList);
    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {
        serverConnection.sendAsync(itemsSelected);
    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {
        serverConnection.sendAsync(acceptedAction);
    }
}
