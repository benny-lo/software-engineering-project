package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

/**
 * Class representing the Game.
 */
public class Game implements GameInterface{
    private int numberPlayers;
    private final int numberGoalCards;
    private Player currentPlayer;
    private Map<String, Player> players;
    private final BoardManager boardManager;
    private CommonGoalCardManager commonGoalCardManager;

    /**
     * Game's Constructor: it initializes {@code Game}.
     * @param numberGoalCards It can be initialised to '1' or '2'.
     */
    public Game(int numberGoalCards) {
        this.numberGoalCards = numberGoalCards;
        this.boardManager = new BoardManager();
    }

    /**
     * This private method creates a {@code PersonalGoalCard} for each {@code Player}.
     */
    private void createPersonalCard(){}
    @Override
    public void addPlayer(String nickname){
        createPersonalCard();
        //players.put(nickname, new Player(nickname, "pattern"))
        this.numberPlayers += 1;
        //da implementare
    }
    @Override
    public void setup(){
        this.commonGoalCardManager = new CommonGoalCardManager(getNumberGoalCards(), getNumberPlayers());
        //da implementare
    }
    @Override
    public void setCurrentPlayer(String nickname){
        currentPlayer = getPlayer(nickname);
    }
    @Override
    public boolean canTakeItemTiles(List<Position> positions){
        return getBoardManager().canTakeItemTiles(positions);
    }
    @Override
    public void selectItemTiles(List<Position> positions){
        //da implementare
    }
    @Override
    public void insertItemTilesInBookshelf(int column, int[] order){
        //da implementare
    }
    @Override
    public String getWinner(){
        int max = 0;
        String winner = null;

        for (String s: players.keySet()){
            if (getScore(s) > max){
                max = getScore(s);
            }
        }

        for (String s: players.keySet()){
            if (getScore(s) == max && getPlayer(s).firstToFinish())
                return s;
            else if (getScore(s) == max) {
                winner = s;
            }
        }
        return winner;
    }
    @Override
    public int getScore(String nickname){
        return getPlayer(nickname).getTotalScore();
    }
    @Override
    public Player getPlayer(String nickname){
        return players.get(nickname);
    }

    @Override
    public int getNumberPlayers(){
        return numberPlayers;
    }

    @Override
    public int getNumberGoalCards() {
        return numberGoalCards;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    @Override
    public BoardManager getBoardManager() {
        return boardManager;
    }
    @Override
    public CommonGoalCardManager getCommonGoalCardManager() {
        return commonGoalCardManager;
    }
}