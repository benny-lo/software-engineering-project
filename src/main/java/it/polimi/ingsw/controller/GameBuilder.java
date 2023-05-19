package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameInterface;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.Map;

public class GameBuilder {
    private final int numberCommonGoalCards;
    private int numberPlayers;
    private final Map<String, Player> players;

    public GameBuilder(int numberCommonGoalCards) {
        this.numberCommonGoalCards = numberCommonGoalCards;
        numberPlayers = 0;
        players = new HashMap<>();
    }

    public void addPlayer(String nickname){
        this.players.put(nickname, new Player(nickname));
        numberPlayers++;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public GameInterface startGame(){
        return new Game(numberCommonGoalCards, numberPlayers, players);
    }
}
