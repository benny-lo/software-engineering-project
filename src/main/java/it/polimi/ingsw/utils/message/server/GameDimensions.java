package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class GameDimensions extends Message {
    private final int livingRoomRows;
    private final int livingRoomColumns;
    private final int bookshelvesRows;
    private final int bookshelvesColumns;

    public GameDimensions(int livingRoomRows, int livingRoomColumns, int bookshelvesRows, int bookshelvesColumns) {
        this.livingRoomRows = livingRoomRows;
        this.livingRoomColumns = livingRoomColumns;
        this.bookshelvesRows = bookshelvesRows;
        this.bookshelvesColumns = bookshelvesColumns;
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
