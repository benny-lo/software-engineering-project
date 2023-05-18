package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Comparable<Position>, Serializable {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(){
        row = -1;
        column = -1;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position other)) return false;

        return (row == other.getRow()) && (column == other.getColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public int compareTo(Position other){
        if (row != other.getRow())
            return row - other.getRow();
        else if (column != other.getColumn())
            return column - other.getColumn();
        return 0;
    }
}
