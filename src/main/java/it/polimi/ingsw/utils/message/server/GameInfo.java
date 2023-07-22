package it.polimi.ingsw.utils.message.server;

import java.io.Serializable;

/**
 * Class storing the constants of the {@code Game}.
 */
public class GameInfo implements Serializable {
    private final int id;
    private final int numberPlayers;
    private final int numberCommonGoals;

    /**
     * Constructor for the class. It sets the id of the game, the number of players and the number of common goal cards.
     * @param id The id of the game.
     * @param numberPlayers The number of players of the game.
     * @param numberCommonGoals The number of common goal cards of the game.
     */
    public GameInfo(int id, int numberPlayers, int numberCommonGoals) {
        this.id = id;
        this.numberPlayers = numberPlayers;
        this.numberCommonGoals = numberCommonGoals;
    }

    /**
     * Getter for the id of the game.
     * @return The id of the game.
     */
    public int getId() { return id; }

    /**
     * Getter for the number of player.
     * @return The number of players of the game.
     */
    public int getNumberPlayers() { return numberPlayers; }

    /**
     * Getter for the number of common goal cards.
     * @return The number of common goal cards.
     */
    public int getNumberCommonGoals() { return numberCommonGoals; }

    /**
     * {@code String} representing the id, the number of players and the number of common goal cards.
     * @return The {@code String} representing {@code this}.
     */
    @Override
    public String toString() {
        return "Id= " + id + ":\n" +
                "   Number of players: " + numberPlayers + "\n" +
                "   Number of common goal cards: " + numberCommonGoals + "\n";
    }
}
