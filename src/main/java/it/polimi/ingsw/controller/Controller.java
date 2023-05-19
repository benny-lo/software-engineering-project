package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatInterface;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.util.*;

public class Controller implements ActionListener {
    /**
     * The model of the game.
     */
    private GameInterface game;

    /**
     * The model of the chat.
     */
    private final ChatInterface chat;
    private final GameBuilder gameBuilder;

    /**
     * The number of players that can join the game.
     */
    private final int numberPlayers;

    /**
     * The number of common goal cards to be used (1 or 2).
     */
    private final int numberCommonGoalCards;

    /**
     * Nickname of the first player. If game has not started yet, it is null.
     */
    private String firstPlayer;

    /**
     * Queue containing the players, the first one is the current player.
     */
    private Queue<String> playerQueue;

    /**
     * Map associating nickname of the players with the virtual views.
     */
    private final Map<String, VirtualView> views;

    /**
     * Flag representing whether the game has ended.
     */
    private boolean ended;

    /**
     * Current turn phase: either selection from living room or selection of column in the bookshelf.
     * If the game has not started yet, it is null.
     */
    private TurnPhase turnPhase;

    public Controller(int numberPlayers, int numberCommonGoalCards) {
        this.numberPlayers = numberPlayers;
        this.game = null;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.chat = new Chat();
        this.gameBuilder = new GameBuilder(numberCommonGoalCards);
        this.firstPlayer = null;
        this.playerQueue = new ArrayDeque<>();
        this.views = new HashMap<>();
        this.turnPhase = null;
        this.ended = false;

        this.bookshelvesListener = new BookshelfListener();
        this.commonGoalCardsListener = new CommonGoalCardsListener();
        this.endingTokenListener = new EndingTokenListener();
        this.livingRoomListener = new LivingRoomListener();
        this.personalGoalCardListeners = new ArrayList<>();
    }

    private void sendBookshelvesToEverybody() {
        if (!bookshelvesListener.hasChanged()) return;
        Map<String, Map<Position, Item>> map = bookshelvesListener.getBookshelves();
        for(String nick : map.keySet()) {
            BookshelfUpdate update = new BookshelfUpdate(nick, map.get(nick));
            for(VirtualView view : views.values()) {
                view.sendBookshelfUpdate(update);
            }
        }
    }

    private void sendLastChatMessageToEverybody() {
        Message message = chat.getLastMessage();
        for(VirtualView view : views.values()) {
            view.sendChatUpdate(new ChatUpdate(
                    message.getNickname(),
                    message.getText())
            );
        }
    }

    private void sendCommonGoalCardsToEverybody() {
        Map<Integer, Integer> map = commonGoalCardsListener.getCards();
        for(VirtualView view : views.values()) {
            view.sendCommonGoalCardUpdate(new CommonGoalCardsUpdate(map));
        }
    }

    private void sendEndingTokenToEverybody() {
        if (!endingTokenListener.hasChanged()) return;
        EndingTokenUpdate update = new EndingTokenUpdate(endingTokenListener.getEndingToken());
        for(VirtualView view : views.values()) {
            view.sendEndingTokenUpdate(update);
        }
    }

    private void sendLivingRoomToEverybody() {
        if (!livingRoomListener.hasChanged()) return;
       LivingRoomUpdate update = new LivingRoomUpdate(livingRoomListener.getLivingRoom());
        for(VirtualView view : views.values()) {
            view.sendLivingRoomUpdate(update);
        }
    }

    private void sendPersonalGoalCardsToEverybody() {
        for(PersonalGoalCardListener listener : personalGoalCardListeners) {
            PersonalGoalCardUpdate update = new PersonalGoalCardUpdate(listener.getPersonalGoalCard());
            views.get(listener.getOwner()).sendPersonalGoalCardUpdate(update);
        }
    }

    private void setupGame() {
        // Choosing a random order for the players.
        List<String> players = new ArrayList<>(playerQueue);
        Collections.shuffle(players);

        // Initializing the queue with the chosen order,
        // and setting the first player.
        playerQueue = new ArrayDeque<>(players);
        firstPlayer = playerQueue.peek();

        // Setting the first player in game and
        // setting the current turn phase.
        game.setCurrentPlayer(firstPlayer);
        turnPhase = TurnPhase.LIVING_ROOM;

        // Initializing the managers and distributing the personal cards.
        game.setup();

        // Set the model listeners in the model.
        game.setBookshelfListener(bookshelvesListener);
        game.setCommonGoalCardsListener(commonGoalCardsListener);
        game.setEndingTokenListener(endingTokenListener);
        game.setLivingRoomListener(livingRoomListener);
        for(PersonalGoalCardListener personalGoalCardListener : personalGoalCardListeners) {
            game.setPersonalGoalCardListener(personalGoalCardListener);
        }

        // Sending initial updates about the reps.
        sendBookshelvesToEverybody();
        sendCommonGoalCardsToEverybody();
        sendEndingTokenToEverybody();
        sendLivingRoomToEverybody();
        sendPersonalGoalCardsToEverybody();

        // to send scores and maybe items chosen null
    }

    private void nextTurn() {
        String justPlayed = playerQueue.poll();
        playerQueue.add(justPlayed);
        game.setCurrentPlayer(playerQueue.peek());
        turnPhase = TurnPhase.LIVING_ROOM;

        assert firstPlayer != null;
        if (firstPlayer.equals(game.getCurrentPlayer()) && game.IsEndingTokenAssigned()) {
            ended = true;

            List<String> nicknames = playerQueue.stream().toList();
            int bestScore = -1;
            String winner = null;
            for(String nickname : nicknames) {
                if (game.getPublicScore(nickname) + game.getPersonalScore(nickname)
                        > bestScore) {
                    winner = nickname;
                }
            }

            for(VirtualView view : views.values()) {
                view.sendEndGameUpdate(new EndGameUpdate(winner));
            }
        } else {
            VirtualView current = views.get(game.getCurrentPlayer());
            current.sendStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
        }
    }

    @Override
    public void update(JoinAction action) {
        if (game != null || ended) {
            action.getView().sendAcceptedAction(new AcceptedAction(false, "SELECT_GAME"));
            return;
        }

        playerQueue.add(action.getSenderNickname());
        //game.addPlayer(action.getSenderNickname());
        gameBuilder.addPlayer(action.getSenderNickname());
        views.put(action.getSenderNickname(), action.getView());
        personalGoalCardListeners.add(new PersonalGoalCardListener(action.getSenderNickname()));

        for(String nick : views.keySet()) {
            views.get(nick).sendWaitingUpdate(new WaitingUpdate(
                    action.getSenderNickname(),
                    numberPlayers - playerQueue.size()
            ));
        }

        if (gameBuilder.getNumberPlayers() == this.numberPlayers) {
            game = gameBuilder.startGame();
            setupGame();
        }
    }

    @Override
    public void update(SelectionFromLivingRoomAction action) {
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(action.getSelectedPositions())) {
            views.get(action.getSenderNickname()).sendItemsSelected(new ItemsSelected(null));
            return;
        }

        List<Item> items = game.selectItemTiles(action.getSelectedPositions());

        for(VirtualView view : views.values()) {
            view.sendItemsSelected(new ItemsSelected(items));
        }

        sendLivingRoomToEverybody();
        turnPhase = TurnPhase.BOOKSHELF;
    }

    @Override
    public void update(SelectionColumnAndOrderAction action) {
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.BOOKSHELF ||
                !game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            views.get(action.getSenderNickname()).sendAcceptedAction(new AcceptedAction(false, "SELECT_BOOKSHELF"));
            return;
        }

        game.insertItemTilesInBookshelf(action.getColumn(), action.getOrder());

        sendLivingRoomToEverybody();
        sendBookshelvesToEverybody();
        sendPersonalGoalCardsToEverybody();
        sendCommonGoalCardsToEverybody();
        sendEndingTokenToEverybody();

        // remember to send scores and maybe items selected as null.

        nextTurn();
    }

    @Override
    public void update(ChatMessageAction action) {
        if (ended || game.getCurrentPlayer() == null) {
            views.get(action.getSenderNickname()).sendAcceptedAction(new AcceptedAction(false, "WRITE_CHAT"));
            return;
        }

        chat.addMessage(action.getSenderNickname(), action.getText());

        sendLastChatMessageToEverybody();
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    public String getCurrentPlayer(){
        return playerQueue.peek();
    }

    public boolean isStarted() {
        return turnPhase != null;
    }

    // EXCLUSIVELY FOR TESTING
    public List<String> getAllPlayers(){
        return new LinkedList<>(playerQueue);
    }

    public void setEnded() {
        ended = true;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public void setCurrentPlayer(String nickname){
        String p;
        assert playerQueue.peek() != null;
        while(!playerQueue.peek().equals(nickname)){
            p = playerQueue.poll();
            playerQueue.add(p);
            assert playerQueue.peek() != null;
        }

        game.setCurrentPlayer(nickname);
    }

    public GameInterface getGame(){
        return game;
    }
}