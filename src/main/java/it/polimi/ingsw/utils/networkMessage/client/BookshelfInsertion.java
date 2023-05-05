package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

import java.util.List;

public class BookshelfInsertion extends NetworkMessageWithSender {
    private final int column;
    private final List<Integer> permutation;

    public BookshelfInsertion(String nickname, int column, List<Integer> permutation) {
        super(nickname);
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
