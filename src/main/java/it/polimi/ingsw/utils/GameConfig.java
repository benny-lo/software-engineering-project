package it.polimi.ingsw.utils;

/**
 * Class representing GameConfig.
 * It contains info about the bookshelves and the living room of the game.
 */
public final class GameConfig {
    private int bookshelfR;
    private int bookshelfC;
    private int livingRoomR;
    private int livingRoomC;

    public GameConfig() {}

    /**
     * Getter of the number of rows of the bookshelves.
     * @return The number of rows.
     */
    public int getBookshelfR() {
        return bookshelfR;
    }

    /**
     * Getter of the number of columns of the bookshelves.
     * @return The number of columns.
     */
    public int getBookshelfC() {
        return bookshelfC;
    }

    /**
     * Getter of the number of rows of the living room.
     * @return The number of rows.
     */
    public int getLivingRoomR() {
        return livingRoomR;
    }

    /**
     * Getter of the number of columns of the living room.
     * @return The number of columns.
     */
    public int getLivingRoomC() {
        return livingRoomC;
    }
}
