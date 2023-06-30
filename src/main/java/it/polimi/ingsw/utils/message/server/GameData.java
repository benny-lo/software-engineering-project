package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Message sent by the server when a client enters a {@code Game}.
 */
public class GameData extends Message {
    private final int numberPlayers;
    private final Collection<String> connectedPlayers;
    private final int numberCommonGoalCards;
    private final int livingRoomRows;
    private final int livingRoomColumns;
    private final int bookshelvesRows;
    private final int bookshelvesColumns;

    /**
     * Constructor for the class. It sets all {@code Game} parameters.
     * @param numberPlayers The number of players.
     * @param connectedPlayers Collection of connected players so far in the joined {@code Game}.
     * @param numberCommonGoalCards The number of common goal cards.
     * @param livingRoomRows The number of the rows of the living room.
     * @param livingRoomColumns The number of the columns of the living room.
     * @param bookshelvesRows The number of the rows of the bookshelves.
     * @param bookshelvesColumns The number of the columns of the bookshelves.
     */
    public GameData(int numberPlayers, Collection<String> connectedPlayers, int numberCommonGoalCards, int livingRoomRows, int livingRoomColumns, int bookshelvesRows, int bookshelvesColumns) {
        this.numberPlayers = numberPlayers;
        this.connectedPlayers = connectedPlayers;
        this.numberCommonGoalCards = numberCommonGoalCards;
        this.livingRoomRows = livingRoomRows;
        this.livingRoomColumns = livingRoomColumns;
        this.bookshelvesRows = bookshelvesRows;
        this.bookshelvesColumns = bookshelvesColumns;
    }

    /**
     * Getter for the number of players.
     * @return The number of players.
     */
    public int getNumberPlayers() {
        return numberPlayers;
    }

    /**
     * Getter for the players connected in this game.
     * @return {@code Collection} of the connected players.
     */
    public Collection<String> getConnectedPlayers() {
        return (connectedPlayers != null) ?
                new ArrayList<>(connectedPlayers) :
                null;
    }

    /**
     * Getter for number of {@code CommonGoalCard}s.
     * @return The number of {@code CommonGoalCard}s
     */
    public int getNumberCommonGoalCards() {
        return numberCommonGoalCards;
    }

    /**
     * Getter for the number of {@code LivingRoom} rows.
     * @return The number of {@code LivingRoom} rows.
     */
    public int getLivingRoomRows() {
        return livingRoomRows;
    }

    /**
     * Getter for the number of {@code LivingRoom} columns.
     * @return The number of {@code LivingRoom} columns.
     */
    public int getLivingRoomColumns() {
        return livingRoomColumns;
    }

    /**
     * Getter for the {@code Bookshelf}s rows.
     * @return The number of {@code Bookshelf}s rows.
     */
    public int getBookshelvesRows() {
        return bookshelvesRows;
    }
    /**
     * Getter for the {@code Bookshelf}s columns.
     * @return The number of {@code Bookshelf}s columns.
     */
    public int getBookshelvesColumns() {
        return bookshelvesColumns;
    }
}
