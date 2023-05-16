package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Sender;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.change.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VirtualView {
    private final String nickname;
    private final BookshelfListener bookshelfListener;
    private final LivingRoomListener livingRoomListener;
    private final EndingTokenListener endingTokenListener;
    private final PersonalGoalCardListener personalGoalCardListener;
    private final CommonGoalCardsListener commonGoalCardsListener;
    private final ItemsChosenListener itemsChosenListener;
    private final ScoreListener scoreListener;
    private final ChatListener chatListener;
    private Controller controller;
    private Sender toClient;
    private boolean error;

    public VirtualView(String nickname) {
        this.nickname = nickname;
        this.bookshelfListener = new BookshelfListener();
        this.livingRoomListener = new LivingRoomListener();
        this.endingTokenListener = new EndingTokenListener();
        this.personalGoalCardListener = new PersonalGoalCardListener(nickname);
        this.commonGoalCardsListener = new CommonGoalCardsListener();
        this.itemsChosenListener = new ItemsChosenListener(nickname);
        this.chatListener = new ChatListener();
        this.scoreListener = new ScoreListener();
        this.controller = null;
        this.error = false;
    }

    public String getNickname() {
        return nickname;
    }

    public BookshelfListener getBookshelfListener() {
        return bookshelfListener;
    }

    public LivingRoomListener getLivingRoomListener() {
        return livingRoomListener;
    }

    public EndingTokenListener getEndingTokenListener() {
        return endingTokenListener;
    }

    public PersonalGoalCardListener getPersonalGoalCardListener() {
        return personalGoalCardListener;
    }

    public CommonGoalCardsListener getCommonGoalCardsListener() {
        return commonGoalCardsListener;
    }

    public ItemsChosenListener getItemsChosenListener() {
        return itemsChosenListener;
    }

    public ScoreListener getScoreListener() {
        return scoreListener;
    }

    public ChatListener getChatListener() {
        return chatListener;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void setToClient(Sender toClient) {
        this.toClient = toClient;
    }

    public void sendBookshelves() {
        if (!livingRoomListener.hasChanged()) return;
        Map<Pair<String, Position>, Item> updates = bookshelfListener.getBookshelves();
        List<String> nicks = updates.keySet().stream().map(Pair::getFirst).toList();

        Map<String, Map<Position, Item>> toSend = new HashMap<>();
        for(String nick : nicks) toSend.put(nick, new HashMap<>());

        for(Pair<String, Position> key : updates.keySet()) {
            toSend.get(key.getFirst()).put(key.getSecond(), updates.get(key));
        }

        for(String owner : toSend.keySet()) {
            toClient.sendBookshelfUpdate(new BookshelfUpdate(owner, toSend.get(owner)));
        }
    }

    public void sendLivingRoom() {
        if (!livingRoomListener.hasChanged()) return;
        toClient.sendLivingRoomUpdate(new LivingRoomUpdate(livingRoomListener.getLivingRoom()));
    }

    public void sendEndingToken() {
        if (!endingTokenListener.hasChanged()) return;
        toClient.sendEndingTokenUpdate(new EndingTokenUpdate(endingTokenListener.getEndingToken()));
    }

    public void sendPersonalGoalCard() {
        if (personalGoalCardListener.hasChanged()) return;
        toClient.sendPersonalGoalCardUpdate(new PersonalGoalCardUpdate(personalGoalCardListener.getPersonalGoalCard()));
    }

    public void sendCommonGoalCard() {
        if (!commonGoalCardsListener.hasChanged()) return;

        toClient.sendCommonGoalCardUpdate(new CommonGoalCardsUpdate(commonGoalCardsListener.getCards()));
    }

    public void sendMessage() {
        if (!chatListener.hasChanged()) return;
        toClient.sendChatUpdate(new ChatUpdate(chatListener.getChat().getNickname(),
                chatListener.getChat().getText()));
    }

    public void sendScores() {
        if (!scoreListener.hasChanged()) return;
        Map<String, Integer> scores = scoreListener.getScores();
        toClient.sendScoresUpdate(new ScoresUpdate(scores));
    }

    public void sendStartTurn(String currentPlayer) {
        toClient.sendStartTurnUpdate(new StartTurnUpdate(currentPlayer));
    }

    public void sendEndGame(String winner) {
        toClient.sendEndGameUpdate(new EndGameUpdate(winner));
    }

    public void sendWaiting(String nick, int missing) {
        toClient.sendWaitingUpdate(new WaitingUpdate(nick, missing));
    }

    public void sendListOfGames(List<GameInfo> gamesList) {
        toClient.sendListOfGames(new GamesList(gamesList));
    }

    public void sendItemsSelected() {
        toClient.sendItemsSelected(new ItemsSelected(itemsChosenListener.getItemsChosen()));
    }

    public void sendAcceptedAction(boolean flag, String description) {
        toClient.sendAcceptedAction(new AcceptedAction(flag, description));
    }

    public void setError() {
        error = true;
    }

    public boolean isError() {
        boolean ret = error;
        error = false;
        return ret;
    }
}
