package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.ChatInterface;
import it.polimi.ingsw.utils.action.*;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class Controller implements ActionListener {
    /**
     * the model.
     */
    private final GameInterface game;
    private final ChatInterface chat;

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
        firstPlayer = playerQueue.peek();

        // Setting the first player in game and
        // setting the current turn phase.
        game.setCurrentPlayer(firstPlayer);
        turnPhase = TurnPhase.LIVING_ROOM;

        // Initializing the managers and distributing the personal cards.
        game.setup();

        // for each view, setting its listeners to the model.
        for(VirtualView view : views.values()) {
            game.setEndingTokenListener(view.getEndingTokenListener());
            game.setBookshelfListener(view.getBookshelfListener());
            game.setCommonGoalCardsListener(view.getCommonGoalCardsListener());
            game.setLivingRoomListener(view.getLivingRoomListener());
            game.setPersonalGoalCardListener(view.getPersonalGoalCardListener());
            game.setItemsChosenListener(view.getItemsChosenListener());
            game.setScoreListener(view.getScoreListener());
            chat.setChatListener(view.getChatListener());
        }

        // Sending initial updates about the reps.
        for(VirtualView view : views.values()) {
            view.sendLivingRoom();
            view.sendBookshelves();
            view.sendPersonalGoalCard();
            view.sendCommonGoalCard();
            view.sendEndingToken();
            view.sendScores();
            view.sendStartTurn(game.getCurrentPlayer());
        }
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
            action.getView().sendAcceptedAction(false, "SELECT_GAME");
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
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.LIVING_ROOM ||
                !game.canTakeItemTiles(action.getSelectedPositions())) {
            views.get(action.getSenderNickname()).sendItemsSelected();
            views.get(action.getSenderNickname()).setError();
            return;
        }

        game.selectItemTiles(action.getSelectedPositions());

        views.get(action.getSenderNickname()).sendItemsSelected();

        for(String nickname : views.keySet()) {
            views.get(nickname).sendLivingRoom();
        }

        turnPhase = TurnPhase.BOOKSHELF;
    }

    @Override
    public void update(SelectionColumnAndOrderAction action) {
        if (ended ||
                !action.getSenderNickname().equals(game.getCurrentPlayer()) ||
                turnPhase != TurnPhase.BOOKSHELF ||
                !game.canInsertItemTilesInBookshelf(action.getColumn(), action.getOrder())) {
            views.get(action.getSenderNickname()).sendAcceptedAction(false, "SELECT_BOOKSHELF");
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
        if (ended || game.getCurrentPlayer() == null) {
            views.get(action.getSenderNickname()).sendAcceptedAction(false, "WRITE_CHAT");
            views.get(action.getSenderNickname()).setError();
            return;
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