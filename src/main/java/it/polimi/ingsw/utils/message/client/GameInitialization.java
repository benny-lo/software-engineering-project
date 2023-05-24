package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class GameInitialization extends Message {
    private final int numberPlayers;
    private final int numberCommonGoalCards;

    /**
     * Constructor for the class
     * @param numberPlayers - number of players that start the game
     * @param numberCommonGoalCards - number of common goal cards needed
     */
    public GameInitialization(int numberPlayers, int numberCommonGoalCards) {
        super();
        this.numberPlayers = numberPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
    }

    /**
     * Getter for the number of players
     * @return - the number of players
     */
    public int getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * Getter for the number of common goal cards
     * @return - the number of common goal cards
     */
    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }
}
