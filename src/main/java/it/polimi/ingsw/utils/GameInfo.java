package it.polimi.ingsw.utils;

public class GameInfo {
    private final int id;
    private final int numberPlayers;
    private int numberPlayersSignedIn;
    private final int numberCommonGoals;

    public GameInfo(int id, int numberPlayers, int numberCommonGoals) {
        this.id = id;
        this.numberPlayers = numberPlayers;
        this.numberCommonGoals = numberCommonGoals;
    }

    public int getId() { return id; }
    public int getNumberPlayers() { return numberPlayers; }

    public int getNumberPlayersSignedIn(){
        return numberPlayersSignedIn;
    }
    public int getNumberCommonGoals() { return numberCommonGoals; }

    public void addPlayer() {
        numberPlayersSignedIn++;
    }
}
