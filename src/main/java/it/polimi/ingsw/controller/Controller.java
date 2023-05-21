package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatInterface;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.utils.message.server.*;

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

    /**
     * Builder of the game model.
     */
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

    /**
     * Listener for the bookshelves.
     */
    private final BookshelvesListener bookshelvesListener;

    /**
     * Listener for the common goal cards.
     */
    private final CommonGoalCardsListener commonGoalCardsListener;

    /**
     * Listener for the ending token.
     */
    private final EndingTokenListener endingTokenListener;

    /**
     * Listener for the LivingRoom.
     */
    private final LivingRoomListener livingRoomListener;

    /**
     * List of listeners for the personal goal cards.
     */
    private final List<PersonalGoalCardListener> personalGoalCardListeners;

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

        this.bookshelvesListener = new BookshelvesListener();
        this.commonGoalCardsListener = new CommonGoalCardsListener();
        this.endingTokenListener = new EndingTokenListener();
        this.livingRoomListener = new LivingRoomListener();
        this.personalGoalCardListeners = new ArrayList<>();

        gameBuilder.setBookshelvesListener(bookshelvesListener);
        gameBuilder.setCommonGoalCardsListener(commonGoalCardsListener);
        gameBuilder.setEndingTokenListener(endingTokenListener);
        gameBuilder.setLivingRoomListener(livingRoomListener);
    }

    private void notifyBookshelvesToEverybody() {
        if (!bookshelvesListener.hasChanged()) return;
        Map<String, Map<Position, Item>> map = bookshelvesListener.getBookshelves();
        for(String nick : map.keySet()) {
            BookshelfUpdate update = new BookshelfUpdate(nick, map.get(nick));
            for(VirtualView view : views.values()) {
                view.onBookshelfUpdate(update);
            }
        }
    }

    private void notifyLastChatMessageToEverybody() {
        Message message = chat.getLastMessage();
        for(VirtualView view : views.values()) {
            view.onChatUpdate(new ChatUpdate(
                    message.getNickname(),
                    message.getText())
            );
        }
    }

    private void notifyCommonGoalCardsToEverybody() {
        Map<Integer, Integer> map = commonGoalCardsListener.getCards();
        for(VirtualView view : views.values()) {
            view.onCommonGoalCardsUpdate(new CommonGoalCardsUpdate(map));
        }
    }

    private void notifyEndingTokenToEverybody() {
        if (!endingTokenListener.hasChanged()) return;
        EndingTokenUpdate update = new EndingTokenUpdate(endingTokenListener.getEndingToken());
        for(VirtualView view : views.values()) {
            view.onEndingTokenUpdate(update);
        }
    }

    private void notifyLivingRoomToEverybody() {
        if (!livingRoomListener.hasChanged()) return;
       LivingRoomUpdate update = new LivingRoomUpdate(livingRoomListener.getLivingRoom());
        for(VirtualView view : views.values()) {
            view.onLivingRoomUpdate(update);
        }
    }

    private void notifyPersonalGoalCardsToEverybody() {
        for(PersonalGoalCardListener listener : personalGoalCardListeners) {
            PersonalGoalCardUpdate update = new PersonalGoalCardUpdate(listener.getPersonalGoalCard());
            views.get(listener.getOwner()).onPersonalGoalCardUpdate(update);
        }
    }

    private void setup() {
        // Choosing a random order for the players.
        List<String> players = new ArrayList<>(playerQueue);
        Collections.shuffle(players);

        // Initializing the queue with the chosen order,
        // and setting the first player.
        playerQueue = new ArrayDeque<>(players);
        firstPlayer = playerQueue.peek();

        game = gameBuilder.startGame();

        // Setting the first player in game and
        // setting the current turn phase.
        game.setCurrentPlayer(firstPlayer);
        turnPhase = TurnPhase.LIVING_ROOM;

        // Sending initial updates about the reps.
        notifyBookshelvesToEverybody();
        notifyCommonGoalCardsToEverybody();
        notifyEndingTokenToEverybody();
        notifyLivingRoomToEverybody();
        notifyPersonalGoalCardsToEverybody();

        // to send scores and maybe items chosen null
    }

    private void nextTurn() {
        String justPlayed = playerQueue.poll();
        playerQueue.add(justPlayed);
        game.setCurrentPlayer(playerQueue.peek());

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
                view.onEndGameUpdate(new EndGameUpdate(winner));
            }
        } else {
            VirtualView current = views.get(game.getCurrentPlayer());
            current.onStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
        }
    }

    @Override
    public synchronized void update(JoinAction action) {
        if (game != null || ended) {
            action.getView().onGameDimensions(new GameDimensions(-1, -1, -1, -1));
            return;
        }

        // TODO: send dimensions.

        playerQueue.add(action.getSenderNickname());
        views.put(action.getSenderNickname(), action.getView());
        personalGoalCardListeners.add(new PersonalGoalCardListener(action.getSenderNickname()));

        gameBuilder.addPlayer(action.getSenderNickname());
        gameBuilder.setPersonalGoalCardListener(personalGoalCardListeners.get(personalGoalCardListeners.size() - 1));

        for(String nick : views.keySet()) {
            views.get(nick).onWaitingUpdate(new WaitingUpdate(
                    action.getSenderNickname(),
                    numberPlayers - playerQueue.size()
            ));
        }

        if (gameBuilder.getCurrentPlayers() == this.numberPlayers) {
            setup();
        }
    }

    @Override
    public synchronized void update(SelectionFromLivingRoomAction action) {
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(action.getSelectedPositions())) {
            views.get(action.getSenderNickname()).onItemsSelected(new ItemsSelected(null));
            return;
        }

        List<Item> items = game.selectItemTiles(action.getSelectedPositions());

        for(VirtualView view : views.values()) {
            view.onItemsSelected(new ItemsSelected(items));
        }

        notifyLivingRoomToEverybody();
        turnPhase = TurnPhase.BOOKSHELF;
    }

    @Override
    public synchronized void update(SelectionColumnAndOrderAction action) {
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.BOOKSHELF ||
                !game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            views.get(action.getSenderNickname()).onAcceptedInsertion(new AcceptedInsertion(false));
            return;
        }

        game.insertItemTilesInBookshelf(action.getColumn(), action.getOrder());

        notifyLivingRoomToEverybody();
        notifyBookshelvesToEverybody();
        notifyPersonalGoalCardsToEverybody();
        notifyCommonGoalCardsToEverybody();
        notifyEndingTokenToEverybody();
        turnPhase = TurnPhase.LIVING_ROOM;

        views.get(action.getSenderNickname()).onAcceptedInsertion(new AcceptedInsertion(true));

        // remember to send scores and maybe items selected as null.

        nextTurn();
    }

    @Override
    public synchronized void update(ChatMessageAction action) {
        if (ended || game.getCurrentPlayer() == null) {
            views.get(action.getSenderNickname()).onChatAccepted(new ChatAccepted(false));
            return;
        }

        chat.addMessage(action.getSenderNickname(), action.getText());

        views.get(action.getSenderNickname()).onChatAccepted(new ChatAccepted(true));

        notifyLastChatMessageToEverybody();
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

    public TurnPhase getTurnPhase(){
        return turnPhase;
    }
}