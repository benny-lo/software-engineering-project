package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;

import java.util.List;

/**
 * Interface representing a {@code Game}. It allows to set the current player and perform moves.
 */

public interface GameInterface {
    /**
     * Sets the current player.
     * @param nickname The nickname of the new current player.
     */
    void setCurrentPlayer(String nickname);

    /**
     * Checks if the list of {@code Position}s can be selected from the {@code LivingRoom} by the current player.
     * @param positions {@code List} of {@code Position}s chosen by a player.
     * @return {@code true} iff the column is available, {@code false} if otherwise.
     */
    boolean canTakeItemTiles(List<Position> positions);

    /**
     * Extracts a list of {@code Item}, chosen by the current player, from the {@code LivingRoom}, and assigns to the
     * current player.
     * @param positions {@code List} of {@code Position}s chosen by the current player.
     * @return The {@code Item}s selected from the {@code LivingRoom}.
     */
    List<Item> selectItemTiles(List<Position> positions);

    /**
     * Checks if we can insert the chosen elements in {@code column} and in the given {@code order}.
     * @param column The column of the bookshelf.
     * @param order A permutation representing the order to insert the elements in.
     * @return {@code true} iff the move is valid.
     */
    boolean canInsertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * Inserts the items selected by the current player in their {@code Bookshelf}.
     * @param column The column to insert in.
     * @param order {@code List} representing the order of the {@code Item}s to insert in {@code Bookshelf}.
     */
    void insertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * Checks if the ending token was assigned to any of the players.
     * @return {@code true} iff there is a player that owns the ending token.
     */
    boolean isEndingTokenAssigned();

    /**
     * Gets the {@code Player}'s public score (bookshelf, common goals and ending token).
     * @param nickname The {@code Players}'s nickname.
     * @return The public score of the {@code Player}.
     */
    int getPublicScore(String nickname);

    /**
     * Gets the {@code Player}'s personal score(personal goal).
     * @param nickname The {@code Player}'s name.
     * @return The personal score of a {@code Player}.
     */
    int getPersonalScore(String nickname);

    /**
     * Gets the id of the {@code PersonalGoalCard} assigned to a {@code Player}.
     * @param nickname The nickname of the player.
     * @return The id of the {@code PersonalGoalCard} of the player with nickname {@code nickname}.
     */
    int getPersonalID(String nickname);

    /**
     * Resets the {@code Item}s selected by the current {@code PLayer} from the {@code LivingRoom}.
     */
    void resetTilesSelected();
}
