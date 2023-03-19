package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.Stack;

/**
 * Class representing the following pattern:
 * Two disjoint groups of four {@code Item}s of the same kind
 * forming a 2x2 square. The {@code Item}s of the two
 * groups have to be of the same kind.
 */
public class CommonGoalPattern1 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        Stack<Position> s = new Stack<>();
        Position curr;
        for(int i = 0; i < bookshelf.getRows()-1; i++) {
            for(int j = 0; j < bookshelf.getColumns()-1; j++) {
                curr = null;
                if (s.size() > 0) curr = s.peek();

                if (!squareOfFour(new Position(i, j), bookshelf)) continue;
                if (curr != null && i - curr.getRow() <= 1 && j - curr.getColumn() <= 1) continue;
                if (curr != null && bookshelf.tileAt(curr) != bookshelf.tileAt(i, j)) continue;
                s.push(new Position(i, j));

                if (s.size() == 2) return true;
            }
        }

        return false;
    }

    /**
     * Check if 2x2 square with given top-left corner has all items of the same kind.
     * @param p the top-left corner.
     * @param bookshelf the bookshelf to check the condition on.
     * @return {@code true} iff the 2x2 square with top-left corner at {@code p} has all non-null items of the same
     * kind.
     */
    private boolean squareOfFour(Position p, Bookshelf bookshelf) {
        if (bookshelf.tileAt(p) == null) return false;
        if (bookshelf.tileAt(p) == bookshelf.tileAt(p.getRow()+1, p.getColumn()) &&
        bookshelf.tileAt(p) == bookshelf.tileAt(p.getRow(), p.getColumn()+1) &&
        bookshelf.tileAt(p) == bookshelf.tileAt(p.getRow()+1, p.getColumn()+1)) {
            return true;
        }
        return false;
    }
}
