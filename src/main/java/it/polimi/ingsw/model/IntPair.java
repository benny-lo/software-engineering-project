package it.polimi.ingsw.model;

public class IntPair {
    private final int row;
    private final int column;

    public IntPair(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }
}
