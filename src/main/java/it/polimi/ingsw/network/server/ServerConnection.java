package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;

public interface ServerConnection {
    void setInputViewInterface(InputViewInterface inputViewInterface);
    void send(LivingRoomUpdate update);
    void send(BookshelfUpdate update);
    void send(WaitingUpdate update);
    void send(ScoresUpdate update);
    void send(EndingTokenUpdate update);
    void send(CommonGoalCardsUpdate update);
    void send(PersonalGoalCardUpdate update);
    void send(ChatUpdate update);
    void send(StartTurnUpdate update);
    void send(EndGameUpdate update);
    void send(GamesList gamesList);
    void send(ItemsSelected itemsSelected);
    void send(GameData gameData);
    void send(AcceptedInsertion acceptedInsertion);
    void send(ChatAccepted chatAccepted);
}
