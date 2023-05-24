package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

import java.util.List;

public class BookshelfInsertion extends Message {
    private final int column;
    private final List<Integer> permutation;

    /**
     * Constructor for the class
     * @param column - column selected
     * @param permutation - the permutation in which the objects are inserted
     */
    public BookshelfInsertion(int column, List<Integer> permutation) {
        super();
        this.column = column;
        this.permutation = permutation;
    }

    /**
     * Getter for the column
     * @return - the column selected
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter for the permutation
     * @return - permutation in which the objects are inserted.
     */
    public List<Integer> getPermutation() {
        return permutation;
    }
}
