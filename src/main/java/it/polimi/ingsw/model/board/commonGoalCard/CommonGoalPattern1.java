package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * Two disjoint groups of four {@code Item}s of the same kind
 * forming a 2x2 square. The {@code Item}s of the two
 * groups have to be of the same kind.
 */
public class CommonGoalPattern1 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        Position p1 = null;
        Position p2 = null;
        for(int i = 0; i < bookshelf.getRows() - 1; i++) {
            for(int j = 0; j < bookshelf.getColumns() - 1; j++) {
                if (bookshelf.tileAt(i, j) == bookshelf.tileAt(i, j+1) &&
                        bookshelf.tileAt(i, j) == bookshelf.tileAt(i+1, j) &&
                        bookshelf.tileAt(i, j) == bookshelf.tileAt(i+1, j+1)) {
                    p1 = new Position(i, j);
                    break;
                }
            }
        }

        if (p1 == null) return false;
        for(int i = p1.getRow(); i < bookshelf.getRows() - 1; i++) {
            for(int j = p1.getColumn(); j < bookshelf.getColumns() - 1; j++) {
                if (i == p1.getRow() && (j == p1.getColumn() || j == p1.getColumn() + 1)) continue;
                if (i == p1.getRow() + 1 && (j == p1.getColumn() || j == p1.getColumn() + 1)) continue;

                if (bookshelf.tileAt(i, j) == bookshelf.tileAt(i, j+1) &&
                        bookshelf.tileAt(i, j) == bookshelf.tileAt(i+1, j) &&
                        bookshelf.tileAt(i, j) == bookshelf.tileAt(i+1, j+1)) {
                    p2 = new Position(i, j);
                    break;
                }
            }
        }

        return p2 != null;
    }
}
