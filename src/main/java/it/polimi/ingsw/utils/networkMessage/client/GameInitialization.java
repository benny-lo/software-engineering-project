package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class GameInitialization extends NetworkMessage {
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
