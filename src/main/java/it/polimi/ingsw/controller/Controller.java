package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.GameConfig;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;
import it.polimi.ingsw.utils.message.server.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Controller of a single game. Its purpose is to construct the model, manage request from clients and
 * appropriately send updates to clients. Some methods synchronize on {@code this}.
 */
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
     * The number of players that have to join the game.
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
     * Set of {@code ServerUpdateViewInterface}s connected to the {@code Game}
     * controlled by {@code this}.
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
     * Listeners for the bookshelves, one per player.
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
     * Constructor for the class. It creates a {@code Controller} for a not yet started (and constructed) game.
     * @param numberPlayers - the number of players that need to join the game to make it start.
     * @param numberCommonGoalCards - the number of common goal cards of the game.
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
     * Notifies the players of any changes in the bookshelves.
     */
    private void notifyBookshelvesToEverybody() {
        for (BookshelfListener bookshelfListener : bookshelfListeners) {
            if (!bookshelfListener.hasChanged()) continue;
            Map<Position, Item> map = bookshelfListener.getBookshelf();
            BookshelfUpdate update = new BookshelfUpdate(bookshelfListener.getOwner(), map);
            for (ServerUpdateViewInterface v : views) {
                v.onBookshelfUpdate(update);
            }
        }
    }

    /**
     * Notifies the players of any changes in the common goal cards.
     */
    private void notifyCommonGoalCardsToEverybody() {
        Map<Integer, Integer> map = commonGoalCardsListener.getCards();
        for(ServerUpdateViewInterface view : views) {
            view.onCommonGoalCardsUpdate(new CommonGoalCardsUpdate(map));
        }
    }

    /**
     * Notifies the players of any changes in the ending token.
     */
    private void notifyEndingTokenToEverybody() {
        if (!endingTokenListener.hasChanged()) return;
        EndingTokenUpdate update = new EndingTokenUpdate(endingTokenListener.getEndingToken());
        for(ServerUpdateViewInterface view : views) {
            view.onEndingTokenUpdate(update);
        }
    }

    /**
     * Notifies the players of any changes in the living room.
     */
    private void notifyLivingRoomToEverybody() {
        if (!livingRoomListener.hasChanged()) return;
       LivingRoomUpdate update = new LivingRoomUpdate(livingRoomListener.getLivingRoom());
        for(ServerUpdateViewInterface view : views) {
            view.onLivingRoomUpdate(update);
        }
    }

    /**
     * Notifies the players of any changes in the scores.
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
     * Notifies the players of any changes in the personal goal cards.
     */
    private void notifyPersonalGoalCardsToEverybody() {
        for (ServerUpdateViewInterface v : views) {
            v.onPersonalGoalCardUpdate(new PersonalGoalCardUpdate(game.getPersonalID(v.getNickname())));
        }
    }

    /**
     * Notifies the players that the game has ended.
     * @param winner the winner; {@code null} if the game terminated due to an error.
     */
    private void notifyEndGame(String winner) {
        for (ServerUpdateViewInterface v : views) {
            v.onEndGameUpdate(new EndGameUpdate(winner));
        }
    }

    /**
     * Helper method to initialize the game: it shuffles the players, constructs the model, sets the turn and
     * sends all start-game updates.
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
        if (game == null) {
            notifyEndGame(null);
            return;
        }

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

        //
        for(ServerUpdateViewInterface v : views) {
            v.onStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
        }
    }

    /**
     * Starts the next turn: move the queue of players one step forward; if the game is ending, notify the players,
     * else notify the beginning of the turn of the new current player.
     */
    private void nextTurn() {

        // Move the queue forward.
        String justPlayed = playerQueue.poll();
        playerQueue.add(justPlayed);
        game.setCurrentPlayer(playerQueue.peek());

        if (firstPlayer.equals(game.getCurrentPlayer()) && game.IsEndingTokenAssigned()) {
            // Game ended -> Find the winner.
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

            notifyEndGame(winner);
        } else {
            // Game continues.
            for(ServerUpdateViewInterface v : views) {
                v.onStartTurnUpdate(new StartTurnUpdate(game.getCurrentPlayer()));
            }
        }
    }

    /**
     * Gets the game config parameters from JSON file.
     * @return {@code GameConfig} object storing the game info (bookshelf and living room dimensions).
     */
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
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param action information passed to {@code Controller} as a {@code JoinAction}.
     */
    @Override
    public synchronized void perform(JoinAction action) {
        if (turnPhase != null || ended) {
            // Game already started or already ended.
            action.getView().onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // Get the GameConfig and send them to player.
        GameConfig gameConfig = getGameConfig();
        action.getView().onGameData(new GameData(numberPlayers, getConnectedPlayers(), numberCommonGoalCards, gameConfig.getLivingRoomR(), gameConfig.getLivingRoomC(), gameConfig.getBookshelfR(), gameConfig.getBookshelfC()));

        // Add player to queue and to list of views.
        playerQueue.add(action.getView().getNickname());
        views.add(action.getView());

        // Update builder with player and listener for their bookshelf.
        gameBuilder.addPlayer(action.getView().getNickname());

        BookshelfListener bookshelfListener = new BookshelfListener(action.getView().getNickname());
        bookshelfListeners.add(bookshelfListener);
        gameBuilder.setBookshelfListener(bookshelfListener);

        // Update all waiting players that a new one just connected.
        for(ServerUpdateViewInterface v : views) {
            v.onWaitingUpdate(new WaitingUpdate(
                    action.getView().getNickname(),
                    numberPlayers - playerQueue.size(),
                    true
            ));
        }

        if (gameBuilder.getCurrentPlayers() == this.numberPlayers) {
            // All players joined.
            setup();
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param action information passed to {@code Controller} as a {@code SelectionFromLivingRoomAction}.
     */
    @Override
    public synchronized void perform(SelectionFromLivingRoomAction action) {
        if (ended ||
                !action.getView().getNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(action.getSelectedPositions())) {
            action.getView().onSelectedItems(new SelectedItems(null));
            return;
        }

        List<Item> items = game.selectItemTiles(action.getSelectedPositions());

        notifyLivingRoomToEverybody();

        // Send the selected items to everybody.
        for(ServerUpdateViewInterface v : views) {
            v.onSelectedItems(new SelectedItems(items));
        }

        // Update the turn phase.
        turnPhase = TurnPhase.BOOKSHELF;
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param action information passed to {@code Controller} as a {@code SelectionColumnAndOrderAction}.
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

        // Set the new turn phase.
        turnPhase = TurnPhase.LIVING_ROOM;

        nextTurn();
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param action information passed to {@code Controller} as a {@code ChatMessageAction}.
     */
    @Override
    public synchronized void perform(ChatMessageAction action) {
        if (ended || game.getCurrentPlayer() == null || action.getReceiver().equals(action.getView().getNickname())) {
            action.getView().onChatAccepted(new ChatAccepted(false));
            // Action failed.
            return;
        }

        ChatUpdate update = new ChatUpdate(action.getView().getNickname(),
                action.getText(),
                action.getReceiver());

        if (action.getReceiver().equals("all")) {
            // Broadcast chat message.
            action.getView().onChatAccepted(new ChatAccepted(true));

            for (ServerUpdateViewInterface v : views) {
                v.onChatUpdate(update);
            }
        } else {
            // Unicast chat message.
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
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param action information passed to {@code Controller} as a {@code DisconnectionAction}.
     */
    @Override
    public synchronized void perform(DisconnectionAction action) {
        // Game already finished.
        if (ended) return;

        if (isStarted()) {
            // Disconnection during game-phase.
            // The game is killed.
            ended = true;
            views.remove(action.getView());
            for (ServerUpdateViewInterface v : views) {
                v.onEndGameUpdate(new EndGameUpdate(null));
            }
            return;
        }

        // Disconnection during pre-game phase.
        // The game is not killed.
        views.remove(action.getView());
        gameBuilder.removePlayer(action.getView().getNickname());
        gameBuilder.removeBookshelfListener(action.getView().getNickname());

        String player = playerQueue.poll(), tmp;
        if (player == null) return;
        playerQueue.add(player);
        do {
            tmp = playerQueue.poll();
            if (action.getView().getNickname().equals(tmp)) {
                break;
            }
            playerQueue.add(tmp);
        } while(!player.equals(tmp));

        for(ServerUpdateViewInterface v : views) {
            v.onWaitingUpdate(new WaitingUpdate(action.getView().getNickname(),
                    numberPlayers - playerQueue.size(),
                    false));
        }
    }

    /**
     * Returns whether the game is started.
     * It synchronizes on {@code this}.
     * @return {@code true} iff the game is started.
     */
    public synchronized boolean isStarted() {
        return turnPhase != null;
    }

    /**
     * Getter for the number of players.
     * It synchronizes on {@code this}.
     * @return the number of players.
     */
    public synchronized int getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * Getter for the number of Common Goal cards.
     * @return the number of common goal cards.
     */
    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    /**
     * Getter for the number of players currently connected.
     * It synchronizes on {@code this}.
     * @return the number of players connected.
     */
    public synchronized int getNumberActualPlayers(){
        return playerQueue.size();
    }

    /**
     * Getter for the list of nicknames of the players connected so far.
     * It synchronizes on {@code this}.
     * @return an {@code List} of nicknames ({@code String}s).
     */
    public synchronized List<String> getConnectedPlayers(){
        return new ArrayList<>(playerQueue);
    }

    // EXCLUSIVELY FOR TESTING
    public boolean getEnded()
    {
        return this.ended;
    }

    public void setEnded() {
        ended = true;
    }

    public void setTurnPhase(TurnPhase turnPhase) {
        this.turnPhase = turnPhase;
    }

    public void setCurrentPlayer(String nickname){
        if (nickname == null) return;
        String p;
        while(!nickname.equals(playerQueue.peek())){
            p = playerQueue.poll();
            playerQueue.add(p);
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