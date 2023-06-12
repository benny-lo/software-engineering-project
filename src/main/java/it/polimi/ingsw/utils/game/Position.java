package it.polimi.ingsw.utils.game;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Comparable<Position>, Serializable {
    private final int row;
    private final int column;

    /**
     * Constructor for the class.
     * @param row - the row of the position on the grid.
     * @param column - the column of the position on the grid.
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Constructor for the class.
     * @param p - the position that we want to assign to the new postion.
     */
    public Position(Position p) {
        this.row = p.row;
        this.column = p.column;
    }

    /**
     * Constructor for the class , for json.
     */
    public Position(){
        row = -1;
        column = -1;
    }

    /**
     * Getter for row
     * @return - the row of the positon.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Getter for column
     * @return - the column of the postion
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Override of the equals methods, checks if one position object indicates the same position of another object
     * @param obj - the object that needs to be checked.
     * @return - true if the object indicates the same position, false if otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position other)) return false;

        return (row == other.getRow()) && (column == other.getColumn());
    }

    /**
     * Returns the hash code for the position
     * @return - the hashcode for the position.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    /**
     * This method compares the Position to another , calculating how far is it.
     * @param other the object to be compared.
     * @return - number of rows the other position is far from, or the number of columns the other position is far from
     * or 0 in case it's the same position.
     */
    @Override
    public int compareTo(Position other){
        if (row != other.getRow())
            return row - other.getRow();
        else if (column != other.getColumn())
            return column - other.getColumn();
        return 0;
    }

    @Override
    public String toString() {
        return "\nrow: " + row + ", column: " + column;
    }
}
