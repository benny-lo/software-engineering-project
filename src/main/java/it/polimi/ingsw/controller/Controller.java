package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.modelListener.*;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.GameConfig;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;
import it.polimi.ingsw.utils.message.server.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

/**
 * Controller of a single game. Its purpose is to schedule the turns, manage request from clients and
 * appropriately send updates to clients. Some methods synchronize on {@code this}.
 */
public class Controller implements ControllerInterface {
    /**
     * The model of the game.
     */
    private GameInterface game;

    /**
     * The controller id.
     */
    private final int id;

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
     * List containing the players in the order of the turns.
     */
    private final List<String> playerList;

    private int playerIndex;

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
     * Constructor for {@code this} class. It creates a {@code Controller} for a not yet started (and constructed) game.
     * @param numberPlayers - the number of players that need to join the game to make it start.
     * @param numberCommonGoalCards - the number of common goal cards of the game.
     */
    public Controller(int numberPlayers, int numberCommonGoalCards, int id) {
        this.numberPlayers = numberPlayers;
        this.id = id;
        this.game = null;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.gameBuilder = new GameBuilder(numberCommonGoalCards);
        this.firstPlayer = null;
        this.playerList = new ArrayList<>();
        this.playerIndex = -1;
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
        Logger.endGame(id);
        for (ServerUpdateViewInterface v : views) {
            v.onEndGameUpdate(new EndGameUpdate(winner));
        }
    }

    /**
     * Helper method to initialize the game: it shuffles the players, constructs the model, sets the turn and
     * sends all start-game updates.
     */
    private void setup() {
        Collections.shuffle(playerList);
        firstPlayer = playerList.get(0);
        playerIndex = 0;

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
            v.onStartTurnUpdate(new StartTurnUpdate(playerList.get(playerIndex)));
        }
    }

    /**
     * Starts the next turn: move the queue of players one step forward; if the game is ending, notify the players,
     * else notify the beginning of the turn of the new current player.
     */
    private void nextTurn() {

        // Move the index forward.
        playerIndex++;
        playerIndex %= playerList.size();

        game.setCurrentPlayer(playerList.get(playerIndex));

        if (firstPlayer.equals(playerList.get(playerIndex)) && game.IsEndingTokenAssigned()) {
            // Game ended -> Find the winner.
            ended = true;

            int bestScore = -1;
            String winner = null;
            for(String nickname : playerList) {
                if (game.getPublicScore(nickname) + game.getPersonalScore(nickname)
                        > bestScore) {
                    winner = nickname;
                    bestScore = game.getPublicScore(nickname) + game.getPersonalScore(nickname);
                }
            }

            notifyScoresToEverybody();
            notifyEndGame(winner);
        } else {
            // Game continues.
            for(ServerUpdateViewInterface v : views) {
                v.onStartTurnUpdate(new StartTurnUpdate(playerList.get(playerIndex)));
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
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    @Override
    public synchronized void join(ServerUpdateViewInterface view) {
        if (turnPhase != null || ended) {
            // Game already started or already ended.
            view.setController(null);
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // Get the GameConfig and send them to player.
        GameConfig gameConfig = getGameConfig();
        view.onGameData(new GameData(numberPlayers, new ArrayList<>(playerList), numberCommonGoalCards, gameConfig.getLivingRoomR(), gameConfig.getLivingRoomC(), gameConfig.getBookshelfR(), gameConfig.getBookshelfC()));

        // Add player to queue and to list of views.
        playerList.add(view.getNickname());
        views.add(view);


        // Update builder with player and listener for their bookshelf.
        gameBuilder.addPlayer(view.getNickname());

        BookshelfListener bookshelfListener = new BookshelfListener(view.getNickname());
        bookshelfListeners.add(bookshelfListener);
        gameBuilder.setBookshelfListener(bookshelfListener);

        // Update all waiting players that a new one just connected.
        for(ServerUpdateViewInterface v : views) {
            v.onWaitingUpdate(new WaitingUpdate(
                    view.getNickname(),
                    numberPlayers - playerList.size(),
                    true
            ));
        }

        if (playerList.size() == this.numberPlayers) {
            // All players joined.
            Logger.startGame(id);
            setup();
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param positions {@code List} of {@code Position}s that are chosen by the player.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    @Override
    public synchronized void livingRoom(List<Position> positions, ServerUpdateViewInterface view) {
        if (ended ||
                !view.getNickname().equals(playerList.get(playerIndex)) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(positions)) {
            view.onSelectedItems(new SelectedItems(null));
            return;
        }

        Logger.selectItems(positions.size(), view.getNickname(), id);

        List<Item> items = game.selectItemTiles(positions);

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
     * @param column The column of the {@code Bookshelf} where to insert the previously chosen {@code Item}s
     * @param permutation {@code List} of {@code Integer}s representing the order in which to insert the {@code Item}s in the {@code Bookshelf}.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    @Override
    public synchronized void bookshelf(int column, List<Integer> permutation, ServerUpdateViewInterface view) {
        if (ended ||
                !view.getNickname().equals(playerList.get(playerIndex)) ||
                turnPhase != TurnPhase.BOOKSHELF ||
                !game.canInsertItemTilesInBookshelf(column, permutation)) {
            view.onAcceptedInsertion(new AcceptedInsertion(false));
            return;
        }

        Logger.selectColumn(column, permutation, view.getNickname(), id);

        game.insertItemTilesInBookshelf(column, permutation);

        view.onAcceptedInsertion(new AcceptedInsertion(true));

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
     * @param text The content of the message.
     * @param receiver The receiver of the message.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    @Override
    public synchronized void chat(String text, String receiver, ServerUpdateViewInterface view) {
        if (ended || playerIndex == -1 || receiver.equals(view.getNickname())) {
            view.onChatAccepted(new ChatAccepted(false));
            // Action failed.
            return;
        }

        ChatUpdate update = new ChatUpdate(view.getNickname(),
                text,
                receiver);

        if (receiver.equals("all")) {
            // Broadcast chat message.
            view.onChatAccepted(new ChatAccepted(true));

            Logger.sendMessage(view.getNickname(), "all", id);

            for (ServerUpdateViewInterface v : views) {
                v.onChatUpdate(update);
            }
        } else {
            // Unicast chat message.
            ServerUpdateViewInterface tmp = null;
            for(ServerUpdateViewInterface v : views) {
                if (v.getNickname().equals(receiver)) {
                    tmp = v;
                    break;
                }
            }

            if (tmp == null) {
                view.onChatAccepted(new ChatAccepted(false));
                return;
            }

            Logger.sendMessage(view.getNickname(), receiver, id);

            view.onChatUpdate(update);
            tmp.onChatUpdate(update);
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param view The {@code ServerUpdateViewInterface} that disconnected.
     */
    @Override
    public synchronized void disconnection(ServerUpdateViewInterface view) {
        // Game already finished.
        if (ended) return;

        if (isStarted()) {
            // Disconnection during game-phase.
            // The game is killed.
            ended = true;
            views.remove(view);
            for (ServerUpdateViewInterface v : views) {
                v.onEndGameUpdate(new EndGameUpdate(null));
            }
            Logger.endGame(id);
            return;
        }

        // Disconnection during pre-game phase.
        // The game is not killed.
        views.remove(view);
        gameBuilder.removePlayer(view.getNickname());
        gameBuilder.removeBookshelfListener(view.getNickname());
        playerList.remove(view.getNickname());

        for(ServerUpdateViewInterface v : views) {
            v.onWaitingUpdate(new WaitingUpdate(view.getNickname(),
                    numberPlayers - playerList.size(),
                    false));
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @return {@code true} iff the game has already started.
     */
    @Override
    public synchronized boolean isStarted() {
        return turnPhase != null;
    }

    /**
     * {@inheritDoc}
     * @return The number of players.
     */
    @Override
    public int getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * {@inheritDoc}
     * @return The number of common goal cards.
     */
    @Override
    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @return The number of currently connected players.
     */
    @Override
    public synchronized int getNumberActualPlayers(){
        return playerList.size();
    }

    // EXCLUSIVELY FOR TESTING

    /**
     * Setter for the ended value, used only for testing. Sets the value of ended to true.
     */
    public void setEnded() {
        ended = true;
    }

    /**
     * Setter for the CurrentPlayer, used only for testing.
     * @param nickname The nickname of the player you want to set as the Current.
     */
    public void setCurrentPlayer(String nickname){
        if (nickname == null) return;
        playerIndex = playerList.indexOf(nickname);
        game.setCurrentPlayer(nickname);
    }
}