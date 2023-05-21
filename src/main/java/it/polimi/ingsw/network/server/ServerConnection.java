package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;

public interface ServerConnection {
    void setInputViewInterface(InputViewInterface inputViewInterface);
    void sendLivingRoomUpdate(LivingRoomUpdate update);
    void sendBookshelfUpdate(BookshelfUpdate update);
    void sendWaitingUpdate(WaitingUpdate update);
    void sendScoresUpdate(ScoresUpdate update);
    void sendEndingTokenUpdate(EndingTokenUpdate update);
    void sendCommonGoalCardsUpdate(CommonGoalCardsUpdate update);
    void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update);
    void sendChatUpdate(ChatUpdate update);
    void sendStartTurnUpdate(StartTurnUpdate update);
    void sendEndGameUpdate(EndGameUpdate update);
    void sendGamesList(GamesList gamesList);
    void sendItemsSelected(ItemsSelected itemsSelected);
    void sendAcceptedAction(AcceptedAction acceptedAction);
    void sendLivingRoomAndBookshelvesDimensions(LivingRoomAndBookshelvesDimensions livingRoomAndBookshelvesDimensions);
}
