package it.polimi.ingsw.model.Player;

import it.polimi.ingsw.model.IntPair;
import it.polimi.ingsw.model.Item;
import java.util.LinkedList;
import java.util.Queue;

public class Bookshelf {
    private final Item[][] bookshelf;

    public Bookshelf() {
        this.bookshelf = new Item[6][5];
        for (int i = 0; i < bookshelf.length; i++) {
            for (int j = 0; j < bookshelf[i].length; j++) {
                bookshelf[i][j] = null;
            }
        }
    }

    public boolean canInsert(int itemsSize, int column) {
        if (itemsSize > 3 || itemsSize < 0) return false;

        for(int i = 0; i < itemsSize; i++) {
            if (bookshelf[i][column] != null) return false;
        }

        return true;
    }

    public void insert(Item item, int column) {
        for(int i = bookshelf.length - 1; i >= 0; i--) {
            if (bookshelf[i][column] == null) {
                bookshelf[i][column] = item;
                return;
            }
        }
    }

    public Item tileAt(int x, int y) {
        return bookshelf[x][y];
    }

    public boolean isFull() {
        for(int i = 0; i < bookshelf.length; i++) {
            for(int j = 0; j < bookshelf[i].length; j++) {
                if (bookshelf[i][j] == null) return false;
            }
        }
        return true;
    }

    public int getBookshelfScore() {
        boolean[][] visited = new boolean[6][5];
        Queue<IntPair> q = new LinkedList<>();

        int currentIslandSize;
        int result = 0;
        for(int i = 0; i < bookshelf.length; i++) {
            for(int j = 0; j < bookshelf[i].length; j++) {
                if (visited[i][j] || bookshelf[i][j] == null) continue;

                currentIslandSize = 0;
                q.add(new IntPair(i, j));

                while(!q.isEmpty()) {
                    IntPair p = q.remove();
                    int row = p.getRow();
                    int column = p.getColumn();

                    visited[row][column] = true;
                    currentIslandSize++;

                    if (row+1 < bookshelf.length && bookshelf[row+1][column] == bookshelf[row][column] && !visited[row+1][column]) {
                        q.add(new IntPair(row+1, column));
                    }
                    if (row-1 > 0 && bookshelf[row-1][column] == bookshelf[row][column] && !visited[row-1][column]) {
                        q.add(new IntPair(row-1, column));
                    }
                    if (column+1 < bookshelf[row].length && bookshelf[row][column+1] == bookshelf[row][column] && !visited[row][column+1]) {
                        q.add(new IntPair(row, column+1));
                    }
                    if (column-1 > 0 && bookshelf[row][column-1] == bookshelf[row][column] && !visited[row][column-1]) {
                        q.add(new IntPair(row, column-1));
                    }
                }

                result += getIslandScore(currentIslandSize);
            }
        }
        return result;
    }

    private int getIslandScore(int islandSize) {
        if (islandSize >= 6) return 8;
        else if (islandSize == 5) return 5;
        else if (islandSize == 4) return 3;
        else if (islandSize == 3) return 2;
        else return 0;
    }
}
