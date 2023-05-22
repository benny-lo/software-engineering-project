package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the LivingRoom of the game. The top row and leftmost column have index 0.
 * The unavailable squares are filled with {@code Item.LOCKED}.
 */
public class LivingRoom {
    private final Item[][] grid;

    /**
     * Class constructor: it sets the prohibited squares and fills the rest of the squares with items.
     * @param numberPlayers number of players in the game.
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

    public LivingRoom() {
        this.grid = new Item[9][9];
    }

    /**
     * Getter for rows.
     * @return private field rows.
     */
    public int getRows() {
        return grid.length;
    }

    /**
     * Getter for columns.
     * @return private field columns.
     */
    public int getColumns() {
        if (grid.length == 0) return 0;
        return grid[0].length;
    }

    /**
     * This method sets the {@code Item} in an empty position.
     * @param item Item is placed in the grid.
     * @param position The coordinates where the {@code Item} is placed.
     */
    public void setTile(Item item, Position position){
        if(grid[position.getRow()][position.getColumn()] == null)
            grid[position.getRow()][position.getColumn()] = item;
    }

    /**
     * Check if there are only isolated tiles in {@code this}.
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
     * Check if a position has at least a non-empty neighbour.
     * @param position the position to check on.
     * @return {@code true} iff {@code position} has a non-empty neighbour.
     */
    private boolean isAlone(Position position) {
        return !((position.getRow()+1 < getRows() && !isEmpty(position.getRow() + 1, position.getColumn())) ||
                (position.getRow()-1 > 0 && !isEmpty(position.getRow() - 1, position.getColumn())) ||
                (position.getColumn()+1 < getColumns() && !isEmpty(position.getRow(), position.getColumn() + 1)) ||
                (position.getColumn()-1 > 0 && !isEmpty(position.getRow(), position.getColumn() - 1)));
    }

    /**
     * Get the item in a position of {@code this}.
     * @param row the row.
     * @param column the column.
     * @return the item in position ({@code row}, {@code column}).
     */
    public Item tileAt(int row, int column) {
        return this.grid[row][column];
    }

    /**
     * Check if a square of {@code this} in a specific position contains an item.
     * @param row the row.
     * @param column the column.
     * @return {@code true} iff an item is NOT found at position ({@code row}, {@code column}).
     */
    private boolean isEmpty(int row, int column) {
        return (grid[row][column] == Item.LOCKED || grid[row][column] == null);
    }

    /**
     * Check if the content of square of {@code this} in a specific position contains a selectable item.
     * @param row the row.
     * @param column the column.
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
     * Check if the content of square of {@code this} in a specific position contains a selectable item.
     * @param position It's the position of the {@code Item}.
     * @return {@code true} iff the content of the square in {@code Position} can be selected.
     */
    public boolean selectable(Position position){
        return selectable(position.getRow(), position.getColumn());
    }

    /**
     * Selection of items from {@code this}.
     * @param positions the positions to select the items from.
     * @return list of selected items.
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
