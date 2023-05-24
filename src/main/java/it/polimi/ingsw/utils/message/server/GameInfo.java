package it.polimi.ingsw.utils.message.server;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final int id;
    private final int numberPlayers;
    private final int numberCommonGoals;

    /**
     * Constructor for the class
     * @param id - the id of the game
     * @param numberPlayers - the number of players of the game
     * @param numberCommonGoals - the number of common goal cards of the game
     */
    public GameInfo(int id, int numberPlayers, int numberCommonGoals) {
        this.id = id;
        this.numberPlayers = numberPlayers;
        this.numberCommonGoals = numberCommonGoals;
    }
    /**
     * Getter for the id of the game
     * @return - the id of the game
     */
    public int getId() { return id; }
    /**
     * Getter for the number of player
     * @return - the number of players of the game.
     */
    public int getNumberPlayers() { return numberPlayers; }
    /**
     * Getter for the number of common goal cards
     * @return - the number of common goal cards
     */
    public int getNumberCommonGoals() { return numberCommonGoals; }

    /**
     * Override of the toString method.
     * @return - the string that encaptulates the game
     */
    @Override
    public String toString() {
        return "Id= " + id + ":\n" +
                "\tNumber of players: " + numberPlayers + "\n" +
                "\tNumber of common goal cards: " + numberCommonGoals;
    }
}
