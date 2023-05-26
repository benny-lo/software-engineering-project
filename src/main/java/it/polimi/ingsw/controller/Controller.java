package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.GameConfig;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;
import it.polimi.ingsw.utils.message.server.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class Controller implements ActionListener {
    /**
     * The model of the game.
     */
    private GameInterface game;

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
     * Set of the virtual views connected to the {@code Game} controlled by {@code this}.
     */
    private final Set<ServerUpdateViewInterface> views;

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
    private final List<BookshelfListener> bookshelfListeners;

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
     * Constructor for the class.
     * @param numberPlayers - the number of players
     * @param numberCommonGoalCards - the number of common goal cards
     */
    public Controller(int numberPlayers, int numberCommonGoalCards) {
        this.numberPlayers = numberPlayers;
        this.game = null;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.gameBuilder = new GameBuilder(numberCommonGoalCards);
        this.firstPlayer = null;
        this.playerQueue = new ArrayDeque<>();
        this.views = new HashSet<>();
        this.turnPhase = null;
        this.ended = false;

        this.bookshelfListeners = new ArrayList<>();
        this.commonGoalCardsListener = new CommonGoalCardsListener();
        this.endingTokenListener = new EndingTokenListener();
        this.livingRoomListener = new LivingRoomListener();

        gameBuilder.setCommonGoalCardsListener(commonGoalCardsListener);
        gameBuilder.setEndingTokenListener(endingTokenListener);
        gameBuilder.setLivingRoomListener(livingRoomListener);
    }

    /**
     * This method notifies the players of any changes in the bookshelves and updates them.
     */
    private void notifyBookshelvesToEverybody() {
        for (BookshelfListener bookshelfListener : bookshelfListeners) {
            if (!bookshelfListener.hasChanged()) return;
            Map<Position, Item> map = bookshelfListener.getBookshelf();
            BookshelfUpdate update = new BookshelfUpdate(bookshelfListener.getOwner(), map);
            for (ServerUpdateViewInterface v : views) {
                v.onBookshelfUpdate(update);
            }
        }
    }

    /**
     * This method notifies the players of any changes in the common goal cards and updates them.
     */
    private void notifyCommonGoalCardsToEverybody() {
        Map<Integer, Integer> map = commonGoalCardsListener.getCards();
        for(ServerUpdateViewInterface view : views) {
            view.onCommonGoalCardsUpdate(new CommonGoalCardsUpdate(map));
        }
    }

    /**
     * This method notifies the players of any changes in the ending token and updates it.
     */
    private void notifyEndingTokenToEverybody() {
        if (!endingTokenListener.hasChanged()) return;
        EndingTokenUpdate update = new EndingTokenUpdate(endingTokenListener.getEndingToken());
        for(ServerUpdateViewInterface view : views) {
            view.onEndingTokenUpdate(update);
        }
    }

    /**
     * This method notifies the players of any changes in the living room and updates it.
     */
    private void notifyLivingRoomToEverybody() {
        if (!livingRoomListener.hasChanged()) return;
       LivingRoomUpdate update = new LivingRoomUpdate(livingRoomListener.getLivingRoom());
        for(ServerUpdateViewInterface view : views) {
            view.onLivingRoomUpdate(update);
        }
    }

    /**
     * This method notifies the players of any changes in the scores and updates them.
     */
    private void notifyScoresToEverybody() {
        Map<String, Integer> scores = new HashMap<>();
        for (ServerUpdateViewInterface v : views) {
            for (ServerUpdateViewInterface u : views) {
                int personalScore = game.getPersonalScore(u.getNickname());
                int publicScore = game.getPublicScore(u.getNickname());

                if (ended || v.getNickname().equals(u.getNickname())) scores.put(u.getNickname(), personalScore + publicScore);
                else scores.put(u.getNickname(), publicScore);
            }
            v.onScoresUpdate(new ScoresUpdate(scores));
            scores = new HashMap<>();
        }
    }

    /**
     * This method notifies the players of any changes in the personal goal cards and updates them.
     */
    private void notifyPersonalGoalCardsToEverybody() {
        for (ServerUpdateViewInterface v : views) {
            v.onPersonalGoalCardUpdate(new PersonalGoalCardUpdate(game.getPersonalID(v.getNickname())));
        }
    }

    /**
     * This method initializes the game, the players' turns , the common goal cards, the personal ones, the living room and
     * the bookshelves.
     */
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
        notifyScoresToEverybody();

        for(ServerUpdateViewInterface v : views) {
            v.onStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
        }

        /* for (VirtualView v : views) {
            v.onItemsSelected(new ItemsSelected(null));
        } */
    }

    /**
     * This method controls the various cases of next turn.
     */
    private void nextTurn() {
        String justPlayed = playerQueue.poll();
        playerQueue.add(justPlayed);
        game.setCurrentPlayer(playerQueue.peek());

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

            for(ServerUpdateViewInterface view : views) {
                view.onEndGameUpdate(new EndGameUpdate(winner));
            }
        } else {
            for(ServerUpdateViewInterface v : views) {
                v.onStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
            }
        }
    }

    private GameConfig getGameConfig(){
        Gson gson = new GsonBuilder().serializeNulls()
                .setPrettyPrinting()
                .disableJdkUnsafe()
                .create();

        GameConfig gameConfig;

        String filename = "/configuration/game_config.json";

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))) {
            gameConfig = gson.fromJson(reader,new TypeToken<GameConfig>(){}.getType());
        } catch(IOException e){
            gameConfig = null;
            System.err.println("""
                    Configuration file for gameConfig not found.
                    The configuration file should be in configuration
                    with name game_config.json""");
        }
        return gameConfig;
    }

    /**
     * This method performs the Join action , letting a new player join the game.
     * @param action - action that sets the listener in motion
     */
    @Override
    public synchronized void perform(JoinAction action) {
        if (game != null || ended) {
            action.getView().onGameData(new GameData(-1, -1, -1, -1, -1, -1));
            return;
        }

        GameConfig gameConfig = getGameConfig();
        action.getView().onGameData(new GameData(numberPlayers, numberCommonGoalCards, gameConfig.getLivingRoomR(), gameConfig.getLivingRoomC(), gameConfig.getBookshelfR(), gameConfig.getBookshelfC()));

        playerQueue.add(action.getView().getNickname());
        views.add(action.getView());

        gameBuilder.addPlayer(action.getView().getNickname());

        BookshelfListener bookshelfListener = new BookshelfListener(action.getView().getNickname());
        bookshelfListeners.add(bookshelfListener);
        gameBuilder.setBookshelfListener(bookshelfListener);

        for(ServerUpdateViewInterface v : views) {
            v.onWaitingUpdate(new WaitingUpdate(
                    action.getView().getNickname(),
                    numberPlayers - playerQueue.size(),
                    true
            ));
        }

        if (gameBuilder.getCurrentPlayers() == this.numberPlayers) {
            setup();
        }
    }

    /**
     * This method performs the SelectionFromLivingRoom action, letting a player choose one or more tiles from the
     * living room.
     * @param action - action that sets the listener in motion
     */
    @Override
    public synchronized void perform(SelectionFromLivingRoomAction action) {
        if (ended ||
                !action.getView().getNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(action.getSelectedPositions())) {
            action.getView().onItemsSelected(new ItemsSelected(null));
            return;
        }

        List<Item> items = game.selectItemTiles(action.getSelectedPositions());

        notifyLivingRoomToEverybody();

        for(ServerUpdateViewInterface v : views) {
            v.onItemsSelected(new ItemsSelected(items));
        }

        turnPhase = TurnPhase.BOOKSHELF;
    }

    /**
     * This method perfroms the SelectionColumnAndOrder action, letting the player choose in which column of the bookshelf
     * and in what order put the items.
     * @param action - action that sets the listener in motion
     */
    @Override
    public synchronized void perform(SelectionColumnAndOrderAction action) {
        if (ended ||
                !action.getView().getNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.BOOKSHELF ||
                !game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            action.getView().onAcceptedInsertion(new AcceptedInsertion(false));
            return;
        }

        game.insertItemTilesInBookshelf(action.getColumn(), action.getOrder());

        action.getView().onAcceptedInsertion(new AcceptedInsertion(true));

        notifyLivingRoomToEverybody();
        notifyBookshelvesToEverybody();
        notifyPersonalGoalCardsToEverybody();
        notifyCommonGoalCardsToEverybody();
        notifyEndingTokenToEverybody();
        notifyScoresToEverybody();
        /* for(VirtualView v : views) {
            v.onItemsSelected(new ItemsSelected(null));
        } */

        // maybe move in nextTurn
        turnPhase = TurnPhase.LIVING_ROOM;

        nextTurn();
    }

    /**
     * This method performs the ChatMessage action, letting the player send and recieve messages.
     * @param action - action that sets the listener in motion
     */
    @Override
    public synchronized void perform(ChatMessageAction action) {
        if (ended || game.getCurrentPlayer() == null) {
            action.getView().onChatAccepted(new ChatAccepted(false));
            return;
        }

        ChatUpdate update = new ChatUpdate(action.getView().getNickname(),
                action.getText(),
                action.getReceiver());

        if (action.getReceiver().equals("all")) {
            action.getView().onChatAccepted(new ChatAccepted(true));

            for (ServerUpdateViewInterface v : views) {
                v.onChatUpdate(update);
            }
        } else {
            ServerUpdateViewInterface view = null;
            for(ServerUpdateViewInterface v : views) {
                if (v.getNickname().equals(action.getReceiver())) {
                    view = v;
                    break;
                }
            }

            if (view == null) {
                action.getView().onChatAccepted(new ChatAccepted(false));
                return;
            }

            action.getView().onChatUpdate(update);
            view.onChatUpdate(update);
        }
    }

    /**
     * This method performs the Disconnection action.
     * @param action - action that sets the listener in motion
     */
    @Override
    public synchronized void perform(DisconnectionAction action) {
        if (ended) return;
        if (!isStarted()){
            views.remove(action.getView());
            gameBuilder.removePlayer(action.getView().getNickname());
            gameBuilder.removeBookshelfListener(action.getView().getNickname());

            String player;
            while (true){
                player = playerQueue.poll();
                if (action.getView().getNickname().equals(player))
                    break;
                else
                    playerQueue.add(player);
            }

            for(ServerUpdateViewInterface v : views)
                v.onWaitingUpdate(new WaitingUpdate(action.getView().getNickname(),numberPlayers - playerQueue.size(), false));

            return;
        }
        ended = true;
        views.remove(action.getView());
        for (ServerUpdateViewInterface v : views) {
            v.onEndGameUpdate(new EndGameUpdate(null));
        }
    }

    /**
     * Getter for the turnPhase.
     * @return - returns the turnPhase, if different from null
     */
    public synchronized boolean isStarted() {
        return turnPhase != null;
    }

    /**
     * Getter for the number of players
     * @return - the number of players
     */
    public synchronized int getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * Getter for the number of Common Goal cards
     * @return - the number of common goal cards
     */
    public synchronized int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    /**
     * Getter for the number of players connected
     * @return - the number of players connected
     */
    public synchronized int getNumberActualPlayers(){
        return playerQueue.size();
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

    public GameBuilder getGameBuilder() {
        return gameBuilder;
    }

    public Set<ServerUpdateViewInterface> getViews() {
        return views;
    }

    public List<BookshelfListener> getBookshelfListeners() {
        return bookshelfListeners;
    }

    public List<String> getBookshelfListenersOwners(){
        return getBookshelfListeners().stream().map(BookshelfListener::getOwner).toList();
    }
}