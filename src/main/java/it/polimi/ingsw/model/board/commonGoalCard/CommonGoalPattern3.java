package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Class representing the following pattern:
 * 4 disjoint groups made by 4 items of the same kind. The kind of groups can
 * be distinct from the kind of another group.
 */
public class CommonGoalPattern3 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        Queue<Position> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[bookshelf.getRows()][bookshelf.getColumns()];

        int currentSize;
        int counter = 0;
        for(int i = 0; i < bookshelf.getRows() && counter < 4; i++) {
            for(int j = 0; j < bookshelf.getColumns() && counter < 4; j++) {
                if (visited[i][j] || bookshelf.tileAt(i, j) == null) continue;

                currentSize = 0;
                q.add(new Position(i, j));

                while(!q.isEmpty()) {
                    Position p = q.remove();
                    int row = p.getRow();
                    int column = p.getColumn();

                    if (visited[row][column]) continue;
                    visited[row][column] = true;
                    currentSize++;

                    if (row+1 < bookshelf.getRows() && bookshelf.tileAt(p) == bookshelf.tileAt(row+1, column)
                            && !visited[row+1][column]) {
                        q.add(new Position(row+1, column));
                    }
                    if (row > 0 && bookshelf.tileAt(p) == bookshelf.tileAt(row-1, column)
                            && !visited[row-1][column]) {
                        q.add(new Position(row-1, column));
                    }
                    if (column+1 < bookshelf.getColumns() && bookshelf.tileAt(p) == bookshelf.tileAt(row, column+1)
                            && !visited[row][column+1]) {
                        q.add(new Position(row, column+1));
                    }
                    if (column > 0 && bookshelf.tileAt(p) == bookshelf.tileAt(row, column-1)
                            && !visited[row][column-1]) {
                        q.add(new Position(row, column-1));
                    }
                }
                counter += currentSize / 4;
            }
        }
        return counter == 4;
    }
}
