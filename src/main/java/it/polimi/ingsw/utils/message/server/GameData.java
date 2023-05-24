package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class GameData extends Message {
    private final int numberPlayers;
    private final int numberCommonGoalCards;
    private final int livingRoomRows;
    private final int livingRoomColumns;
    private final int bookshelvesRows;
    private final int bookshelvesColumns;

    /**
     * Constructor for the class
     * @param numberPlayers - the number of players
     * @param numberCommonGoalCards - the number of common goal cards
     * @param livingRoomRows - the number of the rows of the living room
     * @param livingRoomColumns - the number of the columns of the living room
     * @param bookshelvesRows - the number of the rows of the bookshelves
     * @param bookshelvesColumns - the number of the columns of the bookshelves
     */
    public GameData(int numberPlayers, int numberCommonGoalCards, int livingRoomRows, int livingRoomColumns, int bookshelvesRows, int bookshelvesColumns) {
        this.numberPlayers = numberPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.livingRoomRows = livingRoomRows;
        this.livingRoomColumns = livingRoomColumns;
        this.bookshelvesRows = bookshelvesRows;
        this.bookshelvesColumns = bookshelvesColumns;
    }
    /**
     * Getter for the number of players
     * @return - the number of players
     */
    public int getNumberPlayers() {
        return numberPlayers;
    }
    /**
     * Getter for number of common goal cards
     * @return - the number of common goal cards
     */
    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }
    /**
     * Getter for the Living Room rows
     * @return - the number of living room rows
     */
    public int getLivingRoomRows() {
        return livingRoomRows;
    }
    /**
     * Getter for the Living Room columns
     * @return - the number of living room columns
     */
    public int getLivingRoomColumns() {
        return livingRoomColumns;
    }
    /**
     * Getter for the Bookshelves rows
     * @return - the number of Bookshelves columns
     */
    public int getBookshelvesRows() {
        return bookshelvesRows;
    }
    /**
     * Getter for the Bookshelves Columns
     * @return - the number of Bookshelves rows
     */
    public int getBookshelvesColumns() {
        return bookshelvesColumns;
    }
}
