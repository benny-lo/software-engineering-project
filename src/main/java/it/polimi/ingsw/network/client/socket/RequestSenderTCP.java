package it.polimi.ingsw.network.client.socket;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.client.RequestSender;
import it.polimi.ingsw.network.client.UpdateReceiver;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.net.Socket;
import java.util.List;

public class RequestSenderTCP implements RequestSender, ObjectReceiver {
    private final ServerHandler serverHandler;
    private final UpdateReceiver updateReceiver;

    public RequestSenderTCP(Socket socket, UpdateReceiver updateReceiver) {
        this.serverHandler = new ServerHandler(socket, this);
        this.updateReceiver = updateReceiver;
    }

    public void start() {
        (new Thread(serverHandler)).start();
    }

    @Override
    public void login(String nickname) {
        serverHandler.send(new Nickname(nickname));
    }

    @Override
    public void selectGame(String nickname, int id) {
        serverHandler.send(new GameSelection(nickname, id));
    }

    @Override
    public void createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        serverHandler.send(new GameInitialization(nickname, numberPlayers, numberCommonGoals));
    }

    @Override
    public void selectFromLivingRoom(String nickname, List<Position> position) {
        serverHandler.send(new LivingRoomSelection(nickname, position));
    }

    @Override
    public void putInBookshelf(String nickname, int column, List<Integer> permutation) {
        serverHandler.send(new BookshelfInsertion(nickname, column, permutation));
    }

    @Override
    public void addMessage(String nickname, String text) {
        serverHandler.send(new ChatMessage(nickname, text));
    }

    @Override
    public void receive(Object object) {
        if (object instanceof GamesList) {
            updateReceiver.onGamesList((GamesList) object);
        } else if (object instanceof ItemsSelected) {
            updateReceiver.onItemsSelected((ItemsSelected) object);
        } else if (object instanceof LivingRoomUpdate) {
            updateReceiver.onLivingRoomUpdate((LivingRoomUpdate) object);
        } else if (object instanceof BookshelfUpdate) {
            updateReceiver.onBookshelfUpdate((BookshelfUpdate) object);
        } else if (object instanceof EndingTokenUpdate) {
            updateReceiver.onEndingTokenUpdate((EndingTokenUpdate) object);
        } else if (object instanceof WaitingUpdate) {
            updateReceiver.onWaitingUpdate((WaitingUpdate) object);
        } else if (object instanceof ScoresUpdate) {
            updateReceiver.onScoresUpdate((ScoresUpdate) object);
        } else if (object instanceof CommonGoalCardsUpdate) {
            updateReceiver.onCommonGoalCardUpdate((CommonGoalCardsUpdate) object);
        } else if (object instanceof PersonalGoalCardUpdate) {
            updateReceiver.onPersonalGoalCardUpdate((PersonalGoalCardUpdate) object);
        } else if (object instanceof ChatUpdate) {
            updateReceiver.onChatUpdate((ChatUpdate) object);
        } else if (object instanceof StartTurnUpdate) {
            updateReceiver.onStartTurnUpdate((StartTurnUpdate) object);
        } else if (object instanceof EndGameUpdate) {
            updateReceiver.onEndGameUpdate((EndGameUpdate) object);
        }
    }
}
