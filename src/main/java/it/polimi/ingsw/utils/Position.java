package it.polimi.ingsw.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class representing a pair of coordinates in a 2D space.
 */
public class Position implements Comparable<Position>, Serializable {
    private final int row;
    private final int column;

    /**
     * Constructor for the class. It sets the row and the column.
     * @param row The row (y-coordinate) of the position.
     * @param column The column (x-coordinate) of the position.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * No-args constructor, used for JSON. It creates an invalid position: (-1, -1).
     */
    public Position(){
        row = -1;
        column = -1;
    }

    /**
     * Getter for row.
     * @return The row of {@code this}.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Getter for column.
     * @return The column of {@code this}.
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Two {@code Position}s are equal if they share the same row and column.
     * @param obj The object to check equality with.
     * @return {@code true} iff {@code this} and {@code obj} are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position other)) return false;

        return (row == other.getRow()) && (column == other.getColumn());
    }

    /**
     * Returns the hash code of {@code this}.
     * @return The hashcode for the position.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * Compares the {@code this} to another {@code Position}: {@code this} < {@code other} iff
     * the row of {@code this} is smaller than the row of {@code other} or, in case of equality
     * between the rows, if the column of {@code this} is smaller than the column of {@code other}.
     * @param other The object to compare {@code this} with.
     * @return int < 0 if {@code this} < {@code other},
     * int = 0 if {@code this} = {@code other},
     * else an int > 0.
     */
    @Override
    public int compareTo(Position other){
        if (row != other.getRow())
            return row - other.getRow();
        else if (column != other.getColumn())
            return column - other.getColumn();
        return 0;
    }

    /**
     * Converts {@code this} into a {@code String} for printing.
     * @return The {@code String} corresponding to {@code this}.
     */
    @Override
    public String toString() {
        return "\nrow: " + row + ", column: " + column;
    }
}
