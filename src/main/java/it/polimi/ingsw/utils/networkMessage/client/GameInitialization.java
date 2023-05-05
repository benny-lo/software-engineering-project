package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

public class GameInitialization extends NetworkMessageWithSender {
    private final int numberPlayers;
    private final int numberCommonGoalCards;

    public GameInitialization(String nickname, int numberPlayers, int numberCommonGoalCards) {
        super(nickname);
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
