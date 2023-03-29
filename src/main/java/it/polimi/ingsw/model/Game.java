package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

public class Game {
    private final int numberPlayers;
    private final int numberGoalCards;
    private Player currentPlayer;
    private Map<String, Player> players;

    public Game(int numberPlayers, int numberGoalCards) {
        this.numberPlayers = numberPlayers;
        this.numberGoalCards = numberGoalCards;
    }

    private void createPersonalCards(){}

    public void addPlayer(String nickname){}

    public void setup(){}

    public void setCurrentPlayer(String nickname){
        currentPlayer = getPlayer(nickname);
    }

    public boolean canTakeItemTiles(List<Position> positions){return false;} //da implementare

    public void selectItemTiles(List<Position> positions){}

    public void insertItemTilesInBookshelf(int column, int[] order){}

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

    public int getScore(String nickname){
        return getPlayer(nickname).getTotalScore();
    }

    public Player getPlayer(String nickname){
        return players.get(nickname);
    }
}

