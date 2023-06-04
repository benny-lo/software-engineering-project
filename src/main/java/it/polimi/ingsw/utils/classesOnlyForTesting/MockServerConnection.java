package it.polimi.ingsw.utils.classesOnlyForTesting;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;


import java.util.HashSet;
import java.util.Set;

public class MockServerConnection implements ServerConnection {

    public Set<Message> set;
    public MockServerConnection() {
        set = new HashSet<>();
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface) {

    }

    @Override
    public void send(LivingRoomUpdate update) {
        set.add(update);
    }

    @Override
    public void send(BookshelfUpdate update) {
        set.add(update);
    }

    @Override
    public void send(WaitingUpdate update) {
        set.add(update);
    }

    @Override
    public void send(ScoresUpdate update) {
        set.add(update);
    }

    @Override
    public void send(EndingTokenUpdate update) {
        set.add(update);
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        set.add(update);
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        set.add(update);
    }

    @Override
    public void send(ChatUpdate update) {
        set.add(update);
    }

    @Override
    public void send(StartTurnUpdate update) {
        set.add(update);
    }

    @Override
    public void send(EndGameUpdate update) {
        set.add(update);
    }

    @Override
    public void send(GamesList gamesList) {
        set.add(gamesList);
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        set.add(itemsSelected);
    }

    @Override
    public void send(GameData gameData) {
        set.add(gameData);
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        set.add(acceptedInsertion);
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        set.add(chatAccepted);
    }
}
