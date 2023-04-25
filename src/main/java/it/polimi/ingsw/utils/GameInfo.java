package it.polimi.ingsw.utils;

public class GameInfo {
    private int id;
    private int numberPlayers;
    private int numberCommonGoals;

    public GameInfo(int id, int numberPlayers, int numberCommonGoals) {
        this.id = id;
        this.numberPlayers = numberPlayers;
        this.numberCommonGoals = numberCommonGoals;
    }

    public int getId() { return id; }
    public int getNumberPlayers() { return numberPlayers; }
    public int getNumberCommonGoals() { return numberCommonGoals; }
}
