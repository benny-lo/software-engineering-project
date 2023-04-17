package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;

import java.io.FileNotFoundException;
import java.io.FileReader;
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
     * @param numberPlayers It's the number of players in the game.
     */
    public BoardManager(int numberPlayers) {
        this.endingToken = true;
        this.bag = new Bag(22);

        Gson gson = new GsonBuilder().serializeNulls()
                .disableJdkUnsafe()
                .create();

        String filename = "configuration/livingRoom/living_room_" + numberPlayers + ".json";

        LivingRoom tmp;
        try {
            FileReader reader = new FileReader(filename);
            tmp = gson.fromJson(reader, LivingRoom.class);
        } catch(FileNotFoundException e) {
            tmp = new LivingRoom();
            System.err.println("""
                    Configuration file for livingRoom not found.
                    The configuration file should be in configuration/livingRoom
                    with name living_room_{numberPlayers}""");
        }

        this.livingRoom = tmp;
    }

    /**
     * This method fills the free squares of the {@code LivingRoom} with {@code Item}s, if needed.
     */
    public void fill(){
        if (livingRoom.isRefillNeeded()){
            for(int i = 0; i < livingRoom.getRows(); i++){
                for(int j = 0; j < livingRoom.getColumns(); j++){
                    if (bag.isEmpty()) return;
                    if(livingRoom.tileAt(i, j) == null){
                        livingRoom.setTile(bag.extract(), new Position(i, j));
                    }
                }
            }
        }
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
        List<Position> sortedPosition = positions.stream().sorted().toList();

        boolean horizontal = true, vertical = true;
        for(int i = 1; i < sortedPosition.size(); i++) {
            if (sortedPosition.get(i).getRow() != sortedPosition.get(i-1).getRow() ||
            sortedPosition.get(i).getColumn() != sortedPosition.get(i-1).getColumn() + 1) {
                horizontal = false;
            }

            if (sortedPosition.get(i).getColumn() != sortedPosition.get(i-1).getColumn() ||
            sortedPosition.get(i).getRow() != sortedPosition.get(i-1).getRow() + 1) {
                vertical = false;
            }
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
    public boolean isEndingToken(){
        return endingToken;
    }

    /**
     * This method gives the {@code endingToken} to the first player that has filled their {@code Bookshelf}.
     */
    public void takeEndingToken(){
        endingToken = false;
    }

    /**
     * This method gives the {@code livingRoom}.
     * @return It returns the {@code livingRoom}.
     */
    public LivingRoom getLivingRoom() {
        return livingRoom;
    }
}
