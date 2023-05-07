package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatInterface;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.view.rep.BookshelfRep;

import java.util.*;

public class Controller implements ActionListener {
    /**
     * the model.
     */
    private final GameInterface game;
    private final ChatInterface chat;

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
    private boolean ended;
    /**
     * Current turn phase: either selection from living room or selection of column in the bookshelf.
     * If the game has not started yet, it is null.
     */
    private TurnPhase turnPhase;

    public Controller(int numberPlayers, int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.game = new Game(numberCommonGoalCards);
        this.chat = new Chat();
        this.numberPlayers = numberPlayers;
        this.firstPlayer = null;
        this.playerQueue = new ArrayDeque<>();
        this.views = new HashMap<>();
        this.turnPhase = null;
        this.ended = false;
    }

    private void setupGame() {
        // Choosing a random order for the players.
        List<String> players = new ArrayList<>(playerQueue);
        Collections.shuffle(players);

        // Initializing the queue with the chosen order,
        // and setting the first player.
        playerQueue = new ArrayDeque<>(players);
        String firstPlayer = playerQueue.peek();

        // Setting the first player in game and
        // setting the current turn phase.
        game.setCurrentPlayer(firstPlayer);
        turnPhase = TurnPhase.LIVING_ROOM;

        // Getting the list of all virtual views.
        List<VirtualView> viewsList = new ArrayList<>();
        for(String nickname : views.keySet()) {
            viewsList.add(views.get(nickname));
        }

        // for each view, setting its listeners to the model.
        for(VirtualView view : viewsList) {
            game.setEndingTokenRep(view.getEndingTokenRep());
            for(BookshelfRep rep : view.getBookshelfRep()) {
                for(String nickname : views.keySet()) {
                    rep.setOwner(nickname);
                    game.setBookshelfRep(rep);
                }
            }
            game.setCommonGoalCardsRep(view.getCommonGoalCardsRep());
            game.setLivingRoomRep(view.getLivingRoomRep());
            game.setPersonalGoalCardRep(view.getPersonalGoalRep());
            game.setItemsChosenRep(view.getItemsChosenRep());
            game.setScoreRep(view.getScoreRep());
            chat.setChatListener(view.getChatRep());
        }

        // Sending initial updates about the reps.
        for(VirtualView view : viewsList) {
            view.sendLivingRoom();
            view.sendBookshelves();
            view.sendPersonalGoalCard();
            view.sendCommonGoalCard();
            view.sendEndingToken();
            view.sendScores();
        }

        // Notifying the first player their turn has started.
        views.get(game.getCurrentPlayer()).sendStartTurn(game.getCurrentPlayer());
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

            for(String nick : views.keySet()) {
                views.get(nick).sendEndGame(winner);
            }
        } else {
            VirtualView current = views.get(game.getCurrentPlayer());
            current.sendStartTurn(game.getCurrentPlayer());
        }
    }

    @Override
    public void update(JoinAction action) {
        if (game.getCurrentPlayer() != null || ended) {
            action.getView().setError();
            return;
        }

        playerQueue.add(action.getSenderNickname());
        game.addPlayer(action.getSenderNickname());
        views.put(action.getSenderNickname(), action.getView());

        for(String nick : views.keySet()) {
            views.get(nick).sendWaiting(
                    action.getSenderNickname(),
                    numberPlayers - playerQueue.size()
            );
        }

        if (game.getNumberPlayers() == this.numberPlayers) {
            setupGame();
        }
    }

    @Override
    public void update(ReconnectionAction action) {
        //TODO
    }

    @Override
    public void update(SelectionFromLivingRoomAction action) {
        if (ended) {
            views.get(action.getSenderNickname()).setError();
        }

        if (!action.getSenderNickname().equals(game.getCurrentPlayer())) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        if (turnPhase != TurnPhase.LIVING_ROOM) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        if (!game.canTakeItemTiles(action.getSelectedPositions())) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        game.selectItemTiles(action.getSelectedPositions());

        for(String nickname : views.keySet()) {
            views.get(nickname).sendLivingRoom();
        }

        turnPhase = TurnPhase.BOOKSHELF;
    }

    @Override
    public void update(SelectionColumnAndOrderAction action) {
        if (ended) {
            views.get(action.getSenderNickname()).setError();
        }

        if (action.getSenderNickname().equals(game.getCurrentPlayer())) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        if (turnPhase != TurnPhase.BOOKSHELF) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        if (!game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            views.get(action.getSenderNickname()).setError();
            return;
        }

        game.insertItemTilesInBookshelf(action.getColumn(), action.getOrder());

        for(String nickname : views.keySet()) {
            views.get(nickname).sendLivingRoom();
            views.get(nickname).sendBookshelves();
            views.get(nickname).sendPersonalGoalCard();
            views.get(nickname).sendCommonGoalCard();
            views.get(nickname).sendEndingToken();
            views.get(nickname).sendScores();
        }

        nextTurn();
    }

    @Override
    public void update(ChatMessageAction action) {
        if (ended) {
            views.get(action.getSenderNickname()).setError();
        }

        if (game.getCurrentPlayer() == null) {
            views.get(action.getSenderNickname()).setError();
        }

        chat.addMessage(action.getSenderNickname(), action.getText());

        for(String nick : views.keySet()) {
            views.get(nick).sendMessage();
        }
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
}