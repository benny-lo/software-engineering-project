package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.util.List;

/**
 * Class representing a BoardManager.
 */
public class BoardManager {
    private boolean endingToken;
    private final LivingRoom livingRoom;
    private final Bag bag;

    /**
     * BoardManager's Constructor: it initializes the {@code Bag} and the {@code LivingRoom}.
     */
    public BoardManager(int numberPlayers) {
        this.endingToken = true;
        this.bag = new Bag(22);
        this.livingRoom = new LivingRoom(numberPlayers, bag);
    }

    /**
     * This method fills the free squares of the {@code LivingRoom} with {@code Item}s, if needed.
     */
    public void fill() {
        if (livingRoom.isRefillNeeded()) livingRoom.fill(bag);
    }

    /**
     * This method verifies if the player can take a list of {@code Item}s.
     * @param positions It's a list of {@code Item}'s {@code Position}s.
     * @return It returns a boolean, true iff all the {@code Item} can be taken, else false.
     */
    public boolean canTakeItemTilesBoard(List<Position> positions){
        if (positions.size() == 0 || positions.size() > 3) return false;
        for(Position p: positions){
            if(!livingRoom.selectable(p)) return false;
        }
        return horizontalOrVertical(positions);
    }

    /**
     * Check if a list is made of positions all in a row or column.
     * @param positions the list of positions.
     * @return {@code true} iff the positions are all in the same row/column.
     */
    private boolean horizontalOrVertical(List<Position> positions) {
        int x = positions.get(0).getRow();
        int y = positions.get(0).getColumn();

        boolean horizontal = true, vertical = true;
        for(Position p : positions) {
            if (p.getRow() != x) horizontal = false;
            if (p.getColumn() != y) vertical = false;
        }
        return horizontal || vertical; 
    }

    /**
     * This method extract a list of {@code Item}s from {@code LivingRoom}.
     * @param positions The positions to select the {@code Item} from.
     * @return The list of selected {@code Item}s.
     */
    public List<Item> selectItemTiles(List<Position> positions){
       return livingRoom.selectTiles(positions);
    }

    /**
     * This method checks if someone has taken the {@code endingToken}.
     * @return It returns a boolean, true iff the {@code endingToken} hasn't been taken yet, else false.
     */
    public boolean isEndingTileToken(){
        return endingToken;
    }

    /**
     * This method gives the {@code endingToken} to the first player that has filled their {@code Bookshelf}.
     * @return {@code true} iff the ending token was taken.
     */
    public boolean takeEndingToken(){
        boolean res = endingToken;
        endingToken = false;
        return res;
    }
}
