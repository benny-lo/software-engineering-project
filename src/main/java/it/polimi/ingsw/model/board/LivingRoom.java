package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the {@code LivingRoom} of the game. The top row and leftmost column have index 0.
 * The unavailable squares are filled with {@code Item.LOCKED} and the empty squares are set to {@code null}.
 */
public class LivingRoom {
    private final Item[][] grid;

    /**
     * Constructor for the class. It sets the prohibited squares and fills the rest of the squares with {@code Item}s,
     * following the default configurations of the game.
     * @param numberPlayers Number of players in the game.
     */
    public LivingRoom(int numberPlayers) {
        this.grid = new Item[9][9];

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                grid[i][j] = Item.LOCKED;
            }
        }

        for(int j = 3; j <= 4; j++) grid[1][j] = null;
        for(int j = 3; j <= 5; j++) grid[2][j] = null;
        for(int j = 2; j <= 7; j++) grid[3][j] = null;
        for(int j = 1; j <= 7; j++) grid[4][j] = null;
        for(int j = 1; j <= 6; j++) grid[5][j] = null;
        for(int j = 3; j <= 5; j++) grid[6][j] = null;
        for(int j = 4; j <= 5; j++) grid[7][j] = null;

        if (numberPlayers >= 3) {
            grid[0][3] = null;
            grid[2][2] = null;
            grid[2][6] = null;
            grid[3][8] = null;
            grid[5][0] = null;
            grid[6][2] = null;
            grid[6][6] = null;
            grid[8][5] = null;
        }

        if (numberPlayers >= 4) {
            grid[0][4] = null;
            grid[1][5] = null;
            grid[3][1] = null;
            grid[4][0] = null;
            grid[4][8] = null;
            grid[5][7] = null;
            grid[7][3] = null;
            grid[8][4] = null;
        }
    }

    /**
     * No-args constructor, it initializes the {@code LivingRoom} as 9x9 and completely empty.
     */
    public LivingRoom() {
        this.grid = new Item[9][9];
    }

    /**
     * Getter for the number of rows.
     * @return The number of rows.
     */
    public int getRows() {
        return grid.length;
    }

    /**
     * Getter for the number of columns.
     * @return The number of columns.
     */
    public int getColumns() {
        if (grid.length == 0) return 0;
        return grid[0].length;
    }

    /**
     * Sets the {@code Item} in an empty position.
     * @param item {@code Item} to place in the grid.
     * @param position The {@code Position} where the {@code Item} is placed.
     */
    public void setTile(Item item, Position position){
        if(grid[position.getRow()][position.getColumn()] == null)
            grid[position.getRow()][position.getColumn()] = item;
    }

    /**
     * Checks if there are only isolated tiles in {@code this}.
     * @return {@code true} iff only isolated tiles.
     */
    public boolean isRefillNeeded() {
        for(int i = 0; i < getRows(); i++) {
            for(int j = 0; j < getColumns(); j++) {
                if (!isEmpty(i, j) && !isAlone(new Position(i, j))) return false;
            }
        }
        return true;
    }

    /**
     * Checks if a position has at least a non-empty neighbour.
     * @param position The {@code Position} to check on.
     * @return {@code true} iff {@code position} has a non-empty neighbour.
     */
    private boolean isAlone(Position position) {
        return !((position.getRow()+1 < getRows() && !isEmpty(position.getRow() + 1, position.getColumn())) ||
                (position.getRow()-1 > 0 && !isEmpty(position.getRow() - 1, position.getColumn())) ||
                (position.getColumn()+1 < getColumns() && !isEmpty(position.getRow(), position.getColumn() + 1)) ||
                (position.getColumn()-1 > 0 && !isEmpty(position.getRow(), position.getColumn() - 1)));
    }

    /**
     * Gets the {@code Item} in a position of {@code this}.
     * @param row The row.
     * @param column The column.
     * @return The item in position ({@code row}, {@code column}).
     */
    public Item tileAt(int row, int column) {
        return this.grid[row][column];
    }

    /**
     * Checks if there is an {@code Item} at a position of {@code this}.
     * @param row The row.
     * @param column The column.
     * @return {@code true} iff an item is NOT found at position ({@code row}, {@code column}).
     */
    private boolean isEmpty(int row, int column) {
        return (grid[row][column] == Item.LOCKED || grid[row][column] == null);
    }

    /**
     * Check if the {@code Item} at a position in the grid is selectable.
     * @param row The row.
     * @param column The column.
     * @return {@code true} iff the content of the square in position ({@code row}, {@code column}) can be selected.
     */
    public boolean selectable(int row, int column) {
        if ((row < 0 || row >= getRows()) || (column < 0 || column > getColumns())) return false;
        if (isEmpty(row, column)) return false;

        if (row+1 < getRows() && isEmpty(row+1, column)) return true;
        if (row > 0 && isEmpty(row-1, column)) return true;
        if (column+1 < getColumns() && isEmpty(row, column+1)) return true;
        return column > 0 && isEmpty(row, column - 1);
    }

    /**
     * Checks if the {@code Item} at a {@code Position} in the grid is selectable.
     * @param position The {@code Position} of the {@code Item}.
     * @return {@code true} iff the content of the square in {@code Position} can be selected.
     */
    public boolean selectable(Position position){
        return selectable(position.getRow(), position.getColumn());
    }

    /**
     * Selects of items from the grid at {@code positions}.
     * @param positions {@code List} of {@code Position}s to select the items from.
     * @return {@code List} of selected {@code Item}s.
     */
    public List<Item> selectTiles(List<Position> positions) {
        List<Item> selection = new ArrayList<>();
        for(Position p : positions) {
            selection.add(grid[p.getRow()][p.getColumn()]);
            grid[p.getRow()][p.getColumn()] = null;
        }
        return selection;
    }
}
