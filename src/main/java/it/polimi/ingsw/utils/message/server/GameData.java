package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class GameData extends Message {
    private final int numberPlayers;
    private final int numberCommonGoalCards;
    private final int livingRoomRows;
    private final int livingRoomColumns;
    private final int bookshelvesRows;
    private final int bookshelvesColumns;

    public GameData(int numberPlayers, int numberCommonGoalCards, int livingRoomRows, int livingRoomColumns, int bookshelvesRows, int bookshelvesColumns) {
        this.numberPlayers = numberPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.livingRoomRows = livingRoomRows;
        this.livingRoomColumns = livingRoomColumns;
        this.bookshelvesRows = bookshelvesRows;
        this.bookshelvesColumns = bookshelvesColumns;
    }

    public int getNumberPlayers() {
        return numberPlayers;
    }

    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    public int getLivingRoomRows() {
        return livingRoomRows;
    }

    public int getLivingRoomColumns() {
        return livingRoomColumns;
    }

    public int getBookshelvesRows() {
        return bookshelvesRows;
    }

    public int getBookshelvesColumns() {
        return bookshelvesColumns;
    }
}
