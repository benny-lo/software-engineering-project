package it.polimi.ingsw.model.player;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.game.Item;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Class representing the bookshelf of a player. The bottom row and leftmost column have index 0.
 */
public class Bookshelf{
    /**
     * 2D array of {@code Item}s that are currently in {@code this}.
     */
    private final Item[][] bookshelf;

    /**
     * Constructor for {@code this} class. It initializes {@code this} with all positions free ({@code null}).
     */
    public Bookshelf() {
        this.bookshelf = new Item[6][5];
    }

    /**
     * Constructor of this class, exclusively for testing.
     * @param rows Number of rows.
     * @param columns Number of columns.
     */
    public Bookshelf(int rows, int columns){
        this.bookshelf = new Item[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                bookshelf[i][j] = null;
            }
        }
    }

    /**
     * Check if items can be inserted in a column of {@code this}.
     * @param itemsSize Number of {@code Item}s to insert.
     * @param column Column of {@code this} where the items need to be inserted (0-indexed).
     * @return {@code true} iff {@code itemsSize} items can be inserted in {@code column}.
     */
    public boolean canInsert(int itemsSize, int column) {
        if (itemsSize < 0 || itemsSize > getRows()) return false;
        if (column < 0 || column >= getColumns()) return false;

        for(int i = getRows() - 1; i >= getRows() - itemsSize; i--) {
            if (bookshelf[i][column] != null) return false;
        }

        return true;
    }

    /**
     * Insert an {@code Item} in the first available position in {@code column} in {@code this}.
     * @param item {@code Item} to insert.
     * @param column Column where to insert {@code item}.
     */
    public void insert(Item item, int column) {
        for(int i = 0; i < getRows(); i++) {
            if (bookshelf[i][column] == null) {
                bookshelf[i][column] = item;
                return;
            }
        }
    }

    /**
     * Insert some {@code Item}s in {@code column} of {@code this}.
     * @param items {@code List<Item>} to insert in {@code this} in order from first to last.
     * @param column Column where to insert the {@code Item}s from {@code List<Item>}.
     */
    public void insert(List<Item> items, int column) {
        for(Item item : items) {
            insert(item, column);
        }
    }

    /**
     * Get {@code Item} in a position of {@code this}.
     * @param row Row where to look for.
     * @param column Column where to look for.
     * @return {@code Item} in position {@code row} and {@code column} of {@code this}. If the position is free, it
     * returns {@code null}.
     */
    public Item tileAt(int row, int column) {
        return bookshelf[row][column];
    }

    /**
     * Get {@code Item} in a position of {@code this}.
     * @param position {@code Position} where to look for.
     * @return {@code Item} found at {@code Position} of {@code this}. If no {@code Item} at {@code Position} is found,
     * it returns {@code null}.
     */
    public Item tileAt(Position position) {
        return tileAt(position.getRow(), position.getColumn());
    }

    /**
     * Check if {@code this} has no available positions.
     * @return {@code true} iff {@code this} has no available positions.
     */
    public boolean isFull() {
        for(int i = 0; i < getColumns(); i++) {
            if (bookshelf[getRows() - 1][i] == null) return false;
        }
        return true;
    }

    /**
     * Check if @param column is full.
     * @return {@code true} iff {@code this} has no available positions.
     */
    public boolean isFullCol(int column)
    {
        return tileAt(getRows() - 1, column) != null;
    }

    /**Check if @param row is full
     * @return {@code true} iff {@code this} has no available positions.
     */
    public boolean isFullRow(int row)
    {
        for(int column = 0; column < getColumns(); column++) {
            if (tileAt(row, column) == null) return false;
        }
        return true;
    }

    /**
     * Get the score given by islands of like {@code Item}s in {@code this}.
     * @return Total score achieved by all islands of like {@code Item}s in {@code this}.
     */
    public int getBookshelfScore() {
        boolean[][] visited = new boolean[6][5];
        Queue<Position> q = new ArrayDeque<>();

        int currentIslandSize;
        int result = 0;
        for(int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getColumns(); j++) {
                if (visited[i][j] || bookshelf[i][j] == null) continue;

                currentIslandSize = 0;
                q.add(new Position(i, j));

                while(!q.isEmpty()) {
                    Position p = q.remove();
                    int row = p.getRow();
                    int column = p.getColumn();

                    if (visited[row][column]) continue;
                    visited[row][column] = true;
                    currentIslandSize++;

                    if (row+1 < getRows() && bookshelf[row+1][column] == bookshelf[row][column] && !visited[row+1][column]) {
                        q.add(new Position(row+1, column));
                    }
                    if (row-1 > 0 && bookshelf[row-1][column] == bookshelf[row][column] && !visited[row-1][column]) {
                        q.add(new Position(row-1, column));
                    }
                    if (column+1 < getColumns() && bookshelf[row][column+1] == bookshelf[row][column] && !visited[row][column+1]) {
                        q.add(new Position(row, column+1));
                    }
                    if (column-1 > 0 && bookshelf[row][column-1] == bookshelf[row][column] && !visited[row][column-1]) {
                        q.add(new Position(row, column-1));
                    }
                }

                result += getIslandScore(currentIslandSize);
            }
        }
        return result;
    }

    /**
     * Convert the size of an island into a score.
     * @param islandSize The size of an island.
     * @return Score corresponding to {@code IslandSize}.
     */
    private int getIslandScore(int islandSize) {
        if (islandSize >= 6) return 8;
        else if (islandSize == 5) return 5;
        else if (islandSize == 4) return 3;
        else if (islandSize == 3) return 2;
        else return 0;
    }

    /**
     * Getter for the number of rows of {@code this}.
     * @return Number of rows of {@code this}.
     */
    public int getRows() {
        return bookshelf.length;
    }

    /**
     * Getter for number of columns of {@code this}.
     * @return Number of columns of {@code this}.
     */
    public int getColumns() {
        if (bookshelf.length == 0) return 0;
        return bookshelf[0].length;
    }

}