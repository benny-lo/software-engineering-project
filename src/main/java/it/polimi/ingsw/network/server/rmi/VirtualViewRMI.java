package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.utils.networkMessage.server.*;

public class VirtualViewRMI extends VirtualView {
    private final ServerConnectionRMI serverConnection;

    public VirtualViewRMI(Lobby lobby, ServerConnectionRMI serverConnection) {
        super(lobby);
        this.serverConnection = serverConnection;
    }


    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        serverConnection.sendLivingRoomUpdate(update);
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        serverConnection.sendBookshelfUpdate(update);
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        serverConnection.sendWaitingUpdate(update);
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        serverConnection.sendScoresUpdate(update);
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        serverConnection.sendEndingTokenUpdate(update);
    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) {
        serverConnection.sendCommonGoalCardUpdate(update);
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        serverConnection.sendPersonalGoalCardUpdate(update);
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        serverConnection.sendChatUpdate(update);
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        serverConnection.sendStartTurnUpdate(update);
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        serverConnection.sendEndGameUpdate(update);
    }

    @Override
    public void sendListOfGames(GamesList gamesList) {
        serverConnection.sendListOfGames(gamesList);
    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {
        serverConnection.sendItemsSelected(itemsSelected);
    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {
        serverConnection.sendAcceptedAction(acceptedAction);
    }
}
