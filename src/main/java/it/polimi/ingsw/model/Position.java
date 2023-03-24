package it.polimi.ingsw.model;

import java.util.Objects;

public class Position {
    private final int row;
    private final int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
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
        if (!(obj instanceof Position)) return false;

        Position other = (Position) obj;
        return (row == other.getRow()) && (column == other.getColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
