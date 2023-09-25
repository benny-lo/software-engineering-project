package it.polimi.ingsw.model.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.controller.modellistener.LivingRoomListener;
import javafx.util.Pair;

import java.io.*;
import java.util.List;
import java.util.Objects;

/**
 * Class managing the {@code Bag} and the {@code LivingRoom}.
 */
public class BoardManager {
    private static final int ITEMS_PER_TYPE = 22;
    private boolean endingToken;
    private final LivingRoom livingRoom;
    private final Bag bag;
    private LivingRoomListener livingRoomListener;

    /**
     * Constructor for the class: it initializes the {@code Bag} and the {@code LivingRoom}.
     * @param numberPlayers The number of players in the game.
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
        this.bag = new Bag(ITEMS_PER_TYPE);
        this.livingRoomListener = null;
    }

    /**
     * Fills the free squares of the {@code LivingRoom} with {@code Item}s, if needed.
     */
    public void fill(){
        if (livingRoom.isRefillNeeded()) {
            for(int i = 0; i < livingRoom.getRows(); i++) {
                for(int j = 0; j < livingRoom.getColumns(); j++) {
                    if (bag.isEmpty()) return;

                    if(livingRoom.tileAt(i, j) == null) {
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
     * Verifies if the player can take a list of {@code Position}s.
     * @param positions {@code List} of {@code Position}s chosen.
     * @return {@code true} iff all the {@code Item}s at the selected {@code position} can be taken, else false.
     */
    public boolean canTakeItemTilesBoard(List<Position> positions){
        if (positions.isEmpty() || positions.size() > 3) return false;
        for(Position p: positions){
            if(!livingRoom.selectable(p)) return false;
        }
        return horizontalOrVertical(positions);
    }

    /**
     * Checks if a list is made of positions all in a row or column.
     * @param positions the list of positions.
     * @return {@code true} iff the positions are all in the same row/column.
     */
    private boolean horizontalOrVertical(List<Position> positions) {
        List<Position> sortedPosition = positions.stream().sorted().toList();

        boolean horizontal = true;
        boolean vertical = true;
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
     * Extracts a list of {@code Item}s from {@code LivingRoom}.
     * @param positions {@code List} of {@code Position}s to select the {@code Item}s from.
     * @return {@code Map} of selected {@code Item}s in their {@code Position}s.
     */
    public List<Pair<Position, Item>> selectItemTiles(List<Position> positions) {
        List<Pair<Position, Item>> ret = livingRoom.selectTiles(positions);
        if (livingRoomListener != null) {
            for (Position p : positions) {
                livingRoomListener.updateState(p, livingRoom.tileAt(p.getRow(), p.getColumn()));
            }
        }
        return ret;
    }

    /**
     * Checks if someone has taken the {@code endingToken}.
     * @return {@code true} iff the {@code endingToken} hasn't been taken yet, else false.
     */
    public boolean isEndingToken(){
        return endingToken;
    }

    /**
     * Makes the {@code endingToken} unavailable.
     */
    public void takeEndingToken(){
        endingToken = false;
    }

    /**
     * Sets the given LivingRoomListener and updates with the current state of the {@code LivingRoom}.
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

    /**
     * Replaces some previously chosen tiles into the Living Room (called only in case of a disconnection).
     * @param scheme List of (p, i) with p being a {@code Position} and i begin the {@code Item} to put at p.
     */
    public void resetTiles(List<Pair<Position, Item>> scheme) {
        for(Pair<Position, Item> p : scheme) {
           livingRoom.setTile(p.getValue(), p.getKey());
           livingRoomListener.updateState(p.getKey(), p.getValue());
        }
    }

    // EXCLUSIVELY USED FOR TESTING.

    /**
     * Getter for the {@code LivingRoom}.
     * @return The {@code LivingRoom}.
     */
    public LivingRoom getLivingRoom() {
        return livingRoom;
    }
}
