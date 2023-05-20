package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class GameInitialization extends Message {
    private final int numberPlayers;
    private final int numberCommonGoalCards;

    public GameInitialization(int numberPlayers, int numberCommonGoalCards) {
        super();
        this.numberPlayers = numberPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }
}
