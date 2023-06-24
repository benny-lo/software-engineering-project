package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.controller.modelListener.LivingRoomListener;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * Class representing a BoardManager.
 */
public class BoardManager {
    private boolean endingToken;
    private final LivingRoom livingRoom;
    private final Bag bag;
    private LivingRoomListener livingRoomListener;

    /**
     * BoardManager's Constructor: it initializes the {@code Bag} and the {@code LivingRoom}.
     * @param numberPlayers It's the number of players in the game.
     * @throws IOException Error occurred with I/O for JSON configuration files.
     */
    public BoardManager(int numberPlayers) throws IOException {
        Gson gson = new GsonBuilder().serializeNulls()
                .disableJdkUnsafe()
                .create();

        String filename = "/configuration/livingRoom/living_room_" + numberPlayers + ".json";

        try (Reader reader = new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(filename)))){
            this.livingRoom = gson.fromJson(reader, LivingRoom.class);
        } catch(IOException e) {
            System.err.println("""
                    Configuration file for livingRoom not found.
                    The configuration file should be in configuration/livingRoom
                    with name living_room_{numberPlayers}""");
            throw new IOException();
        }

        this.endingToken = true;
        this.bag = new Bag(22);
        this.livingRoomListener = null;
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
                        Position p = new Position(i, j);
                        livingRoom.setTile(bag.extract(), p);
                        if (livingRoomListener != null) {
                            livingRoomListener.updateState(p, livingRoom.tileAt(p.getRow(), p.getColumn()));
                        }
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
    public List<Item> selectItemTiles(List<Position> positions) {
        if (livingRoomListener != null) {
            for (Position p : positions) {
                livingRoomListener.updateState(new Position(p), null);
            }
        }
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

    /**
     * This method sets the given LivingRoomListener, by updating the state of every tile.
     * @param livingRoomListener LivingRoomListener that needs to be set
     */
    public void setLivingRoomListener(LivingRoomListener livingRoomListener) {
        this.livingRoomListener = livingRoomListener;
        for(int i = 0; i < livingRoom.getRows(); i++) {
            for(int j = 0; j < livingRoom.getColumns(); j++) {
                livingRoomListener.updateState(new Position(i, j), livingRoom.tileAt(i, j));
            }
        }
    }
}
