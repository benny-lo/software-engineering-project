package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.List;

public class BookshelfInsertion extends NetworkMessage {
    private final int column;
    private final List<Integer> permutation;

    public BookshelfInsertion(int column, List<Integer> permutation) {
        super();
        this.column = column;
        this.permutation = permutation;
    }

    public int getColumn() {
        return column;
    }

    public List<Integer> getPermutation() {
        return permutation;
    }
}
