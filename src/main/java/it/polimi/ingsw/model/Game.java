package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class representing the Game.
 */
public class Game implements GameInterface {
    private int numberPlayers;
    private final int numberCommonGoalCards;
    private String currentPlayer;
    private final Map<String, Player> players;
    private BoardManager boardManager;
    private CommonGoalCardManager commonGoalCardManager;

    /**
     * Game's Constructor: it initializes {@code Game}.
     * @param numberCommonGoalCards It can be initialised to '1' or '2'.
     */
    public Game(int numberCommonGoalCards) {
        this.numberPlayers = 0;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.currentPlayer = null;
        this.players = new HashMap<>();
        this.boardManager = null;
        this.commonGoalCardManager = null;
    }

    /**
     * This private method creates a {@code PersonalGoalCard} for each {@code Player}.
     */
    private void distributePersonalCards() {
        // TODO: distribute the personal goal cards to the players.
    }

    @Override
    public void addPlayer(String nickname){
        this.players.put(nickname, new Player());
        this.numberPlayers += 1;
    }

    @Override
    public void setup(){
        this.commonGoalCardManager = new CommonGoalCardManager(numberCommonGoalCards, numberPlayers);
        this.boardManager = new BoardManager(numberPlayers);
        this.distributePersonalCards();
    }

    @Override
    public void setCurrentPlayer(String nickname){
        currentPlayer = nickname;
    }

    @Override
    public boolean canTakeItemTiles(List<Position> positions){
        if (positions.size() == 0 || positions.size() > 3) return false;
        if (!boardManager.canTakeItemTilesBoard(positions)) return false;

        boolean availableColumn = false;
        for(int i = 0; i < players.get(currentPlayer).getBookshelf().getColumns(); i++) {
            if (players.get(currentPlayer).getBookshelf().canInsert(positions.size(), i)) availableColumn = true;
        }
        return availableColumn;
    }

    @Override
    public void selectItemTiles(List<Position> positions){
        //TODO: take the item tiles and give them to the current player.
    }

    @Override
    public void insertItemTilesInBookshelf(int column, List<Integer> order) {
        //TODO: insert the items in the column and in the right order.
    }

    @Override
    public String getWinner() {
        boolean nobodyFinished = true;
        for(String s : players.keySet()) {
            if (players.get(s).firstToFinish()) nobodyFinished = false;
        }
        if (!nobodyFinished) return null;

        int max = -1;
        String winner = null;

        for (String s: players.keySet()){
            if (players.get(s).getTotalScore() > max){
                max = players.get(s).getTotalScore();
            }
        }

        for (String s: players.keySet()){
            if (players.get(s).getTotalScore() == max && players.get(s).firstToFinish())
                return s;
            else if (players.get(s).getTotalScore() == max) {
                winner = s;
            }
        }
        return winner;
    }

    @Override
    public int getPublicScore(String nickname) {
        return this.players.get(nickname).getPublicScore();
    }

    @Override
    public int getPersonalScore(String nickname) {
        return this.players.get(nickname).getPersonalScore();
    }

    @Override
    public String getCurrentPlayer() {
        return currentPlayer;
    }
}