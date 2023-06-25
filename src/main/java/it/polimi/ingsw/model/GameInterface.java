package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;

import java.util.List;

/**
 * Interface representing a {@code Game}.
 */

public interface GameInterface {
    /**
     * It sets the {@code currentPlayer}.
     * @param nickname {@code nickname} is the {@code currentPlayer}'s name.
     */
    void setCurrentPlayer(String nickname);

    /**
     * This method says if a player can take a list of {@code Item}s.
     * @param positions {@code positions} is a list of {@code Position}s chosen by a player.
     * @return It returns a boolean, true iff the player is allowed to take the list of {@code Item}, else false.
     */
    boolean canTakeItemTiles(List<Position> positions);

    /**
     * This method extracts a list of {@code Item}, chosen by a player, from the {@code LivingRoom}.
     * @param positions {@code positions} is a list of {@code Position}s chosen by a player.
     * @return The items selected from the LivingRoom.
     */
    List<Item> selectItemTiles(List<Position> positions);

    /**
     * Check if we can insert the chosen elements in {@code column} and in the given {@code order}.
     * @param column The column of the bookshelf.
     * @param order A permutation representing the order to insert the elements in.
     * @return {@code true} iff the move is valid.
     */
    boolean canInsertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * This method inserts the items selected by the current player in its bookshelf.
     * @param column The column to insert in.
     * @param order The list representing a permutation of the items.
     */
    void insertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * Method to check if the ending token was assigned to any of the players.
     * @return {@code true} iff there is a player that owns the ending token.
     */
    boolean IsEndingTokenAssigned();

    /**
     * This method gets the {@code Player}'s public score(bookshelf, common goals and ending token).
     * @param nickname It's the {@code Players}'s name.
     * @return It returns the public score of a {@code Player}.
     */
    int getPublicScore(String nickname);

    /**
     * This method gets the {@code Player}'s personal score(personal goal).
     * @param nickname The {@code Player}'s name.
     * @return The personal score of a {@code Player}.
     */
    int getPersonalScore(String nickname);

    /**
     * This method returns the id of the {@code PersonalGoalCard} assigned to a {@code Player}.
     * @param nickname The nickname of the player.
     * @return The id of the {@code PersonalGoalCard} of the player with nickname {@code nickname}.
     */
    int getPersonalID(String nickname);
}
