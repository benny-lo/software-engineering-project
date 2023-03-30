package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;

import java.util.List;

/**
 * Class representing a BoardManager.
 */
public class BoardManager {
    private final ScoringToken endingToken;
    private final LivingRoom livingRoom;
    private final Bag bag;

    /**
     * BoardManager's Constructor: it initializes the {@code Bag} and the {@code LivingRoom}.
     */
    public BoardManager() {
        this.endingToken = new ScoringToken(1,-1);
        this.bag = new Bag(22);
        this.livingRoom = new LivingRoom(1, getBag());
    }

    /**
     * This method fills the free squares of the {@code LivingRoom} with {@code Item}s.
     */
    public void fill(){
        getLivingRoom().fill(getBag());
    }

    /**
     * This method verifies if the player can take a list of {@code Item}s.
     * @param positions It's a list of {@code Item}s' {@code Position}s.
     * @return It returns a boolean, true iff all the {@code Item} can be taken, else false.
     */
    public boolean canTakeItemTiles(List<Position> positions){
        for(Position p: positions){
            if(!getLivingRoom().selectable(p))
                return false;
        }
        return true;
    }

    /**
     * This method extract a list of {@code Item}s from {@code LivingRoom}.
     * @param positions The positions to select the {@code Item} from.
     * @return The list of selected {@code Item}s.
     */
    public List<Item> selectItemTiles(List<Position> positions){
       return getLivingRoom().selectTiles(positions);
    }

    /**
     * This method checks if someone has taken the {@code endingToken}.
     * @return It returns a boolean, true iff the {@code endingToken} hasn't been taken yet, else false.
     */
    public boolean isEndingTileToken(){
        return getEndingToken() != null;
    }

    /**
     * This method gives the {@code endingToken} to the first player that has filled their {@code Bookshelf}.
     * @return It returns a {@code endingToken}.
     */
    public ScoringToken takeEndingToken(){
        return getEndingToken();
    }

    /**
     * This method returns game's {@code LivingRoom}
     * @return It returns the {@code LivingRoom}.
     */
    public LivingRoom getLivingRoom() {
        return livingRoom;
    }

    /**
     * This method returns {@code Bag}
     * @return It returns the {@code Bag}.
     */
    public Bag getBag() {
        return bag;
    }

    /**
     * This method returns {@code ScoringToken}.
     * @return It returns the {@code endingToken}.
     */
    public ScoringToken getEndingToken() {
        return endingToken;
    }
}
