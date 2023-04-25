package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class Controller implements ActionListener {
    /**
     * the model.
     */
    private final GameInterface game;

    /**
     * The maximum number of players that can join the game.
     */
    private final int numberPlayers;
    private final int numberCommonGoalCards;

    /**
     * Nickname of the first player. If game has not started yet, it is null.
     */
    private final String firstPlayer;

    /**
     * Queue containing the players, the first one is the current player.
     */
    private Queue<String> playerQueue;
    private final Map<String, VirtualView> views;

    /**
     * Current turn phase: either selection from living room or selection of column in the bookshelf.
     * If the game has not started yet, it is null.
     */
    private TurnPhase turnPhase;

    public Controller(int NumberPlayers, int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.game = new Game(numberCommonGoalCards);
        this.numberPlayers = NumberPlayers;
        this.firstPlayer = null;
        this.playerQueue = new ArrayDeque<>();
        this.views = new HashMap<>();
        this.turnPhase = null;
    }

    private void setupGame() {
        game.setup();
        List<String> players = new ArrayList<>(playerQueue);
        Collections.shuffle(players);

        playerQueue = new ArrayDeque<>(players);
        String firstPlayer = playerQueue.peek();

        game.setCurrentPlayer(firstPlayer);
        turnPhase = TurnPhase.LIVING_ROOM;

        // TODO: notify to game.getCurrentPlayer
    }

    private void nextTurn() {
        String justPlayed = playerQueue.poll();
        playerQueue.add(justPlayed);
        game.setCurrentPlayer(playerQueue.peek());
        turnPhase = TurnPhase.LIVING_ROOM;

        // TODO: notify previous player of end turn.

        if (firstPlayer.equals(game.getCurrentPlayer()) && game.getWinner() != null) {
            // TODO: endgame logic.
        } else {
            // TODO: notify current player of start turn.
        }
    }

    @Override
    public void update(JoinAction action) {
        if (firstPlayer == null) {
            // TODO: notify error to player.
            return;
        }

        playerQueue.add(action.getSenderNickname());
        game.addPlayer(action.getSenderNickname());
        views.put(action.getSenderNickname(), action.getView());

        if (game.getNumberPlayers() == this.numberPlayers) {
            setupGame();
        }
    }

    @Override
    public void update(ReconnectionAction action) {
        // TODO
    }

    @Override
    public void update(SelectionFromLivingRoomAction action) {
        if (!action.getSenderNickname().equals(game.getCurrentPlayer())) {
            // TODO: send error to player.
            return;
        }

        if (turnPhase != TurnPhase.LIVING_ROOM) {
            // TODO: send error to player.
            return;
        }

        if (!game.canTakeItemTiles(action.getSelectedPositions())) {
            // TODO: send error to player.
            return;
        }

        game.selectItemTiles(action.getSelectedPositions());
        turnPhase = TurnPhase.BOOKSHELF;

        // TODO: request bookshelf info.
    }

    @Override
    public void update(SelectionColumnAndOrderAction action) {
        if (action.getSenderNickname().equals(game.getCurrentPlayer())) {
            // TODO: send error to player.
            return;
        }

        if (turnPhase != TurnPhase.BOOKSHELF) {
            // TODO: send error to player.
            return;
        }

        if (!game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            // TODO: send error to player.
            return;
        }

        game.insertItemTilesInBookshelf(action.getColumn(), action.getOrder());

        nextTurn();
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }
}

