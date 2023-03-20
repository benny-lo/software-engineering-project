package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Item;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Class representing the bookshelf of a player. The bottom row and leftmost column have index 0.
 */
public class Bookshelf {
    /**
     * 2D array of {@code Item}s that are currently in {@code this}.
     */
    private final Item[][] bookshelf;

    /**
     * number of rows of {@code this}.
     */
    private final int rows;

    /**
     * number of columns of {@code this}.
     */
    private final int columns;

    /**
     * Construct of the class. It initializes {@code this} with all positions free ({@code null}).
     */
    public Bookshelf(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        this.bookshelf = new Item[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                bookshelf[i][j] = null;
            }
        }
    }

    /**
     * Check if items can be inserted in a column of {@code this}.
     * @param itemsSize number of {@code Item}s to insert.
     * @param column column of {@code this} where the items need to be inserted (0-indexed).
     * @return {@code true} iff {@code itemsSize} items can be inserted in {@code column}.
     */
    public boolean canInsert(int itemsSize, int column) {
        if (itemsSize < 0 || itemsSize > rows) return false;
        if (column < 0 || column >= columns) return false;

        for(int i = rows - 1; i >= rows - itemsSize; i--) {
            if (bookshelf[i][column] != null) return false;
        }

        return true;
    }

    /**
     * Insert an {@code Item} in the first available position in {@code column} in {@code this}.
     * @param item {@code Item} to insert.
     * @param column column where to insert {@code item}.
     */
    public void insert(Item item, int column) {
        for(int i = 0; i < rows; i++) {
            if (bookshelf[i][column] == null) {
                bookshelf[i][column] = item;
                return;
            }
        }
    }

    /**
     * Insert some {@code Item}s in {@code column} of {@code this}.
     * @param items {@code List<Item>} to insert in {@code this} in order from first to last.
     * @param column column where to insert the {@code Item}s from {@code List<Item>}.
     */
    public void insert(List<Item> items, int column) {
        for(Item item : items) {
            insert(item, column);
        }
    }

    /**
     * Get {@code Item} in a position of {@code this}.
     * @param row row where to look for.
     * @param column column where to look for.
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
        for(int i = 0; i < columns; i++) {
            if (bookshelf[rows - 1][i] == null) return false;
        }
        return true;
    }

    /**
     * Get the score given by islands of like {@code Item}s in {@code this}.
     * @return total score achieved by all islands of like {@code Item}s in {@code this}.
     */
    public int getBookshelfScore() {
        boolean[][] visited = new boolean[6][5];
        Queue<Position> q = new ArrayDeque<>();

        int currentIslandSize;
        int result = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
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

                    if (row+1 < rows && bookshelf[row+1][column] == bookshelf[row][column] && !visited[row+1][column]) {
                        q.add(new Position(row+1, column));
                    }
                    if (row-1 > 0 && bookshelf[row-1][column] == bookshelf[row][column] && !visited[row-1][column]) {
                        q.add(new Position(row-1, column));
                    }
                    if (column+1 < columns && bookshelf[row][column+1] == bookshelf[row][column] && !visited[row][column+1]) {
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
     * @param islandSize the size of an island.
     * @return score corresponding to {@code IslandSize}.
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
     * @return number of rows of {@code this}.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Getter for number of columns of {@code this}.
     * @return number of columns of {@code this}.
     */
    public int getColumns() {
        return columns;
    }
}
