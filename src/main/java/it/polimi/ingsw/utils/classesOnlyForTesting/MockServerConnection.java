package it.polimi.ingsw.utils.classesOnlyForTesting;

import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;


import java.util.ArrayList;
import java.util.List;

public class MockServerConnection implements ServerConnection {

    public List<Message> list;
    public MockServerConnection() {
        list = new ArrayList<>();
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface serverInputViewInterface) {

    }

    @Override
    public void send(LivingRoomUpdate update) {
        list.add(update);
    }

    @Override
    public void send(BookshelfUpdate update) {
        list.add(update);
    }

    @Override
    public void send(WaitingUpdate update) {
        list.add(update);
    }

    @Override
    public void send(ScoresUpdate update) {
        list.add(update);
    }

    @Override
    public void send(EndingTokenUpdate update) {
        list.add(update);
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        list.add(update);
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        list.add(update);
    }

    @Override
    public void send(ChatUpdate update) {
        list.add(update);
    }

    @Override
    public void send(StartTurnUpdate update) {
        list.add(update);
    }

    @Override
    public void send(EndGameUpdate update) {
        list.add(update);
    }

    @Override
    public void send(GamesList gamesList) {
        list.add(gamesList);
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        list.add(itemsSelected);
    }

    @Override
    public void send(GameData gameData) { list.add(gameData);
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        list.add(acceptedInsertion);
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        list.add(chatAccepted);
    }
}
