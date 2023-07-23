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
    private static final int WIN_FORFEIT = 30000;

    private final Object controllerLock;
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

    /**
     * Index in {@code playerList} of the current player.
     */
    private int playerIndex;

    /**
     * Map of nickname -> {@code ServerUpdateViewInterface}s connected to the {@code Game}.
     * controlled by {@code this}.
     */
    private final Map<String, ServerUpdateViewInterface> views;

    /**
     * Set of the nicknames of the inactive players, i.e. players that disconnected after the game started.
     */
    private final Set<String> inactivePlayers;

    /**
     * Flag representing whether the game has ended.
     */
    private boolean ended;

    /**
     * Flag representing whether the controller has scheduled a timer after only one player connected remained.
     */
    private boolean onHold;

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
     * Listener to the Chat.
     */
    private final ChatListener chatListener;

    /**
     * Timer activated when only one player remains, after the game has started.
     */
    private final Timer timer;

    /**
     * Constructor for {@code this} class. It creates a {@code Controller} for a not yet started (and constructed) game.
     * @param numberPlayers - the number of players that need to join the game to make it start.
     * @param numberCommonGoalCards - the number of common goal cards of the game.
     * @param id The id assigned to the {@code Game} controlled.
     */
    public Controller(int numberPlayers, int numberCommonGoalCards, int id) {
        this.controllerLock = new Object();
        this.numberPlayers = numberPlayers;
        this.id = id;
        this.game = null;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.gameBuilder = new GameBuilder(numberCommonGoalCards);
        this.firstPlayer = null;
        this.playerList = new ArrayList<>();
        this.playerIndex = -1;
        this.views = new HashMap<>();
        this.inactivePlayers = new HashSet<>();
        this.turnPhase = null;
        this.ended = false;

        this.bookshelfListeners = new ArrayList<>();
        this.commonGoalCardsListener = new CommonGoalCardsListener();
        this.endingTokenListener = new EndingTokenListener();
        this.livingRoomListener = new LivingRoomListener();
        this.chatListener = new ChatListener();

        this.timer = new Timer();

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
            Map<Position, Item> map = bookshelfListener.getBookshelfUpdates();
            BookshelfUpdate update = new BookshelfUpdate(bookshelfListener.getOwner(), map);
            for (ServerUpdateViewInterface v : views.values()) {
                v.onBookshelfUpdate(update);
            }
        }
    }

    /**
     * Notifies the players of any changes in the common goal cards.
     */
    private void notifyCommonGoalCardsToEverybody() {
        Map<Integer, Integer> map = commonGoalCardsListener.getCardsUpdates();
        for(ServerUpdateViewInterface view : views.values()) {
            view.onCommonGoalCardsUpdate(new CommonGoalCardsUpdate(map));
        }
    }

    /**
     * Notifies the players of any changes in the ending token.
     */
    private void notifyEndingTokenToEverybody() {
        if (!endingTokenListener.hasChanged()) return;
        EndingTokenUpdate update = new EndingTokenUpdate(endingTokenListener.getEndingToken());
        for(ServerUpdateViewInterface view : views.values()) {
            view.onEndingTokenUpdate(update);
        }
    }

    /**
     * Notifies the players of any changes in the living room.
     */
    private void notifyLivingRoomToEverybody() {
        if (!livingRoomListener.hasChanged()) return;
        LivingRoomUpdate update = new LivingRoomUpdate(livingRoomListener.getLivingRoomUpdates());
        for(ServerUpdateViewInterface view : views.values()) {
            view.onLivingRoomUpdate(update);
        }
    }

    /**
     * Notifies the players of any changes in the scores.
     */
    private void notifyScoresToEverybody() {
        Map<String, Integer> scores;
        for (String v : views.keySet()) {
            scores = new HashMap<>();
            if (inactivePlayers.contains(v)) continue;
            for (String u : views.keySet()) {
                if (inactivePlayers.contains(u)) continue;
                int personalScore = game.getPersonalScore(u);
                int publicScore = game.getPublicScore(u);

                if (ended || v.equals(u)) scores.put(u, personalScore + publicScore);
                else scores.put(u, publicScore);
            }
            views.get(v).onScoresUpdate(new ScoresUpdate(scores));
        }
    }

    /**
     * Notifies the players of any changes in the personal goal cards.
     */
    private void notifyPersonalGoalCardsToEverybody() {
        for (String v : views.keySet()) {
            views.get(v).onPersonalGoalCardUpdate(new PersonalGoalCardUpdate(game.getPersonalID(v)));
        }
    }

    /**
     * Notifies all players of the start of a new turn.
     */
    private void notifyStartTurnToEverybody() {
        for(ServerUpdateViewInterface v : views.values()) {
            v.onStartTurnUpdate(new StartTurnUpdate(playerList.get(playerIndex)));
        }
    }

    /**
     * Notifies the players that the game has ended.
     * @param winner the winner; {@code null} if the game terminated due to an error.
     */
    private void notifyEndGameToEverybody(String winner) {
        Logger.endGame(id);
        for (ServerUpdateViewInterface v : views.values()) {
            v.onEndGameUpdate(new EndGameUpdate(winner));
        }
    }

    /**
     * Notifies all players that a new player connected or disconnected in the pre-game phase.
     * @param nickname The nickname of the player whose state changed, i.e. they connected or disconnected.
     * @param connected {@code true} iff the player connected.
     */
    private void notifyWaitingUpdateToEverybody(String nickname, boolean connected) {
        for(ServerUpdateViewInterface v : views.values()) {
            v.onWaitingUpdate(new WaitingUpdate(
                    nickname,
                    numberPlayers - playerList.size(),
                    connected
            ));
        }
    }

    /**
     * Notifies to all players the {@code Item}s that the current player has selected from the {@code LivingRoom}.
     * @param selectedItems The selected {@code Item}s.
     */
    private void notifySelectedItemsToEverybody(List<Item> selectedItems) {
        for(ServerUpdateViewInterface v : views.values()) {
            v.onSelectedItems(new SelectedItems(selectedItems));
        }
    }

    private void notifyChatUpdate(ChatUpdate update) {
        for(ServerUpdateViewInterface v : views.values()) {
            v.onChatUpdate(update);
        }
    }

    private void notifyDisconnectionToEverybody(String nickname) {
        Disconnection message = new Disconnection(nickname);
        for(ServerUpdateViewInterface v : views.values()) {
            v.onDisconnectionUpdate(message);
        }
    }

    private void notifyReconnectionToEverybody(String nickname) {
        Reconnection update = new Reconnection(nickname);
        for(ServerUpdateViewInterface v : views.values()) {
            v.onReconnectionUpdate(update);
        }
    }

    private void notifyFullState(String nickname) {
        ServerUpdateViewInterface v = views.get(nickname);

        GameConfig gc = getGameConfig();
        v.onGameData(new GameData(numberPlayers,
                                new ArrayList<>(playerList),
                                numberCommonGoalCards,
                                gc.getLivingRoomR(),
                                gc.getLivingRoomC(),
                                gc.getBookshelfR(),
                                gc.getBookshelfC()));

        for(BookshelfListener b : bookshelfListeners) {
            v.onBookshelfUpdate(new BookshelfUpdate(b.getOwner(), b.getBookshelfState()));
        }
        v.onCommonGoalCardsUpdate(new CommonGoalCardsUpdate(commonGoalCardsListener.getCardsState()));
        v.onEndingTokenUpdate(new EndingTokenUpdate(endingTokenListener.getEndingToken()));
        v.onLivingRoomUpdate(new LivingRoomUpdate(livingRoomListener.getLivingRoomState()));
        v.onPersonalGoalCardUpdate(new PersonalGoalCardUpdate(game.getPersonalID(nickname)));

        for(String n : inactivePlayers) {
            Disconnection disconnection = new Disconnection(n);
            v.onDisconnectionUpdate(disconnection);
        }

        notifyScoresToEverybody();

        if (!onHold) {
            v.onStartTurnUpdate(new StartTurnUpdate(playerList.get(playerIndex)));
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
            notifyEndGameToEverybody(null);
            ended = true;
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
        notifyStartTurnToEverybody();
    }

    /**
     * Starts the next turn: move the queue of players one step forward; if the game is ending, notify the players,
     * else notify the beginning of the turn of the new current player.
     */
    private void nextTurn() {
        if (inactivePlayers.size() >= numberPlayers - 1) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (controllerLock) {
                        if (inactivePlayers.size() < numberPlayers - 1) return;
                        ended = true;

                        String winner = null;
                        for (String s : playerList) {
                            if (!inactivePlayers.contains(s)) winner = s;
                        }
                        if (winner != null) notifyEndGameToEverybody(winner);
                    }
                }
            }, WIN_FORFEIT);
            onHold = true;
            return;
        }

        // Move the index forward.
        playerIndex++;
        playerIndex %= playerList.size();
        while(inactivePlayers.contains(playerList.get(playerIndex))) {
            playerIndex++;
            playerIndex %= playerList.size();
        }

        game.setCurrentPlayer(playerList.get(playerIndex));
        turnPhase = TurnPhase.LIVING_ROOM;

        if (firstPlayer.equals(playerList.get(playerIndex)) && game.IsEndingTokenAssigned()) {
            // Game ended -> Find the winner.
            ended = true;

            int bestScore = -1;
            String winner = null;
            for(String nickname : playerList) {
                if (inactivePlayers.contains(nickname)) continue;
                if (game.getPublicScore(nickname) + game.getPersonalScore(nickname)
                        > bestScore) {
                    winner = nickname;
                    bestScore = game.getPublicScore(nickname) + game.getPersonalScore(nickname);
                }
            }

            notifyScoresToEverybody();
            notifyEndGameToEverybody(winner);
        } else {
            // Game continues.
            notifyStartTurnToEverybody();
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
    public boolean join(ServerUpdateViewInterface view, String nickname) {
        synchronized (controllerLock) {
            if (views.containsKey(nickname) || turnPhase != null || ended) {
                // Game already started or already ended or player already joined.
                view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
                return false;
            }

            view.setController(this);

            // Get the GameConfig and send them to player.
            GameConfig gameConfig = getGameConfig();
            view.onGameData(new GameData(numberPlayers, new ArrayList<>(playerList), numberCommonGoalCards, gameConfig.getLivingRoomR(), gameConfig.getLivingRoomC(), gameConfig.getBookshelfR(), gameConfig.getBookshelfC()));

            // Add player to queue and to list of views.
            playerList.add(nickname);
            views.put(nickname, view);


            // Update builder with player and listener for their bookshelf.
            gameBuilder.addPlayer(nickname);

            BookshelfListener bookshelfListener = new BookshelfListener(nickname);
            bookshelfListeners.add(bookshelfListener);
            gameBuilder.setBookshelfListener(bookshelfListener);

            // Update all waiting players that a new one just connected.
            notifyWaitingUpdateToEverybody(nickname, true);

            if (playerList.size() == this.numberPlayers) {
                // All players joined.
                Logger.startGame(id);
                setup();
            }

            return true;
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param positions {@code List} of {@code Position}s that are chosen by the player.
     * @param nickname The nickname of the player performing the action.
     */
    @Override
    public void livingRoom(List<Position> positions, String nickname) {
        synchronized (controllerLock) {
            if (ended || onHold ||
                    !nickname.equals(playerList.get(playerIndex)) ||
                    turnPhase != TurnPhase.LIVING_ROOM ||
                    !game.canTakeItemTiles(positions)) {
                views.get(nickname).onSelectedItems(new SelectedItems(null));
                return;
            }

            Logger.selectItems(positions.size(), nickname, id);

            List<Item> items = game.selectItemTiles(positions);

            notifyLivingRoomToEverybody();

            // Send the selected items to everybody.
            notifySelectedItemsToEverybody(items);

            // Update the turn phase.
            turnPhase = TurnPhase.BOOKSHELF;
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param column The column of the {@code Bookshelf} where to insert the previously chosen {@code Item}s
     * @param permutation {@code List} of {@code Integer}s representing the order in which to insert the {@code Item}s in the {@code Bookshelf}.
     * @param nickname The nickname of the player performing the action.
     */
    @Override
    public void bookshelf(int column, List<Integer> permutation, String nickname) {
        synchronized (controllerLock) {
            if (ended || onHold ||
                    !nickname.equals(playerList.get(playerIndex)) ||
                    turnPhase != TurnPhase.BOOKSHELF ||
                    !game.canInsertItemTilesInBookshelf(column, permutation)) {
                views.get(nickname).onAcceptedInsertion(new AcceptedInsertion(false));
                return;
            }

            Logger.selectColumn(column, permutation, nickname, id);

            game.insertItemTilesInBookshelf(column, permutation);

            views.get(nickname).onAcceptedInsertion(new AcceptedInsertion(true));

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
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param text The content of the message.
     * @param receiver The receiver of the message.
     * @param nickname The nickname of the player performing the action.
     */
    @Override
    public void chat(String text, String receiver, String nickname) {
        synchronized (controllerLock) {
            if (ended || playerIndex == -1 || receiver.equals(nickname)) {
                views.get(nickname).onChatAccepted(new ChatAccepted(false));
                // Action failed.
                return;
            }

            ChatUpdate update = new ChatUpdate(nickname,
                    text,
                    receiver);

            chatListener.addUpdate(update);

            if (receiver.equals("all")) {
                // Broadcast chat message.
                views.get(nickname).onChatAccepted(new ChatAccepted(true));

                Logger.sendMessage(nickname, "all", id);

                notifyChatUpdate(update);
            } else {
                // Unicast chat message.
                ServerUpdateViewInterface other = views.get(receiver);

                if (other == null) {
                    views.get(nickname).onChatAccepted(new ChatAccepted(false));
                    return;
                }

                Logger.sendMessage(nickname, receiver, id);

                views.get(nickname).onChatUpdate(update);
                other.onChatUpdate(update);
            }
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}.
     * @param nickname The nickname of the player that disconnected.
     */
    @Override
    public void disconnection(String nickname) {
        synchronized (controllerLock) {
            // Game already finished.
            if (ended || !views.containsKey(nickname)) return;

            views.remove(nickname);

            if (isStarted()) {
                // Disconnection during game-phase.
                // The game is killed
                inactivePlayers.add(nickname);

                notifyDisconnectionToEverybody(nickname);

                if (inactivePlayers.size() >= numberPlayers) {
                    ended = true;
                    notifyEndGameToEverybody(null);
                    Logger.endGame(id);
                    return;
                }

                notifyScoresToEverybody();

                if (playerList.get(playerIndex).equals(nickname)) {
                    if (turnPhase == TurnPhase.BOOKSHELF) {
                        game.resetTilesSelected();
                        notifyLivingRoomToEverybody();
                    }
                    nextTurn();
                }
            } else {
                // Disconnection during pre-game phase.
                // The game is not killed.
                gameBuilder.removePlayer(nickname);
                gameBuilder.removeBookshelfListener(nickname);
                bookshelfListeners.removeIf(b -> b.getOwner().equals(nickname));
                playerList.remove(nickname);

                notifyWaitingUpdateToEverybody(nickname, false);
            }
        }
    }

    @Override
    public boolean reconnection(ServerUpdateViewInterface view, String nickname) {
        synchronized (controllerLock) {
            if (ended || !inactivePlayers.contains(nickname)) return false;

            view.setController(this);
            views.put(nickname, view);
            inactivePlayers.remove(nickname);

            notifyReconnectionToEverybody(nickname);

            notifyFullState(nickname);

            if (onHold) {
                onHold = false;
                nextTurn();
            }

            return true;
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