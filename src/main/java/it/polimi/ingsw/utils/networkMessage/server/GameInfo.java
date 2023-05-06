package it.polimi.ingsw.utils.networkMessage.server;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final int id;
    private final int numberPlayers;
    private final int numberCommonGoals;

    public GameInfo(int id, int numberPlayers, int numberCommonGoals) {
        this.id = id;
        this.numberPlayers = numberPlayers;
        this.numberCommonGoals = numberCommonGoals;
    }

    public int getId() { return id; }
    public int getNumberPlayers() { return numberPlayers; }

    public int getNumberCommonGoals() { return numberCommonGoals; }
}
