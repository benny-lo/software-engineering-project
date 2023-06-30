package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message sent by the client representing an insertion in the {@code Bookshelf}.
 */
public class BookshelfInsertion extends Message {
    private final int column;
    private final List<Integer> permutation;

    /**
     * Constructor for the class. It sets the column and the permutation, i.e. the order.
     * @param column column selected
     * @param permutation the permutation in which the objects are inserted
     */
    public BookshelfInsertion(int column, List<Integer> permutation) {
        super();
        this.column = column;
        this.permutation =  permutation;
    }

    /**
     * Getter for the column.
     * @return The column selected.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter for the permutation.
     * @return The permutation in which the objects are inserted.
     */
    public List<Integer> getPermutation() {
        return new ArrayList<>(permutation);
    }
}
