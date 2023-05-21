package it.polimi.ingsw.utils.classesOnlyForTesting;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;

public class MockServerConnection implements ServerConnection {
    public MockServerConnection() {

    }

    @Override
    public void setInputViewInterface(InputViewInterface inputViewInterface) {

    }

    @Override
    public void send(LivingRoomUpdate update) {

    }

    @Override
    public void send(BookshelfUpdate update) {

    }

    @Override
    public void send(WaitingUpdate update) {

    }

    @Override
    public void send(ScoresUpdate update) {

    }

    @Override
    public void send(EndingTokenUpdate update) {

    }

    @Override
    public void send(CommonGoalCardsUpdate update) {

    }

    @Override
    public void send(PersonalGoalCardUpdate update) {

    }

    @Override
    public void send(ChatUpdate update) {

    }

    @Override
    public void send(StartTurnUpdate update) {

    }

    @Override
    public void send(EndGameUpdate update) {

    }

    @Override
    public void send(GamesList gamesList) {

    }

    @Override
    public void send(ItemsSelected itemsSelected) {

    }

    @Override
    public void send(GameDimensions gameDimensions) {

    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {

    }

    @Override
    public void send(ChatAccepted chatAccepted) {

    }
}
