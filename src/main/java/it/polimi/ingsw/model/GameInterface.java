package it.polimi.ingsw.model;

import it.polimi.ingsw.view.modelListener.*;

import java.util.List;

/**
 * Interface representing a {@code Game}.
 */

public interface GameInterface {
    /**
     * This method starts the game, and it initializes a {@code PersonalGoalCard} for each {@code Player}.
     */
    void setup();

    /**
     * Getter for the number of players.
     * @return the number of players of {@code this}.
     */
    int getNumberPlayers();

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
     */
    void selectItemTiles(List<Position> positions);

    /**
     * Check if we can insert the chosen elements in {@code column} and in the given {@code order}.
     * @param column the column of the bookshelf.
     * @param order a permutation representing the order to insert the elements in.
     * @return {@code true} iff the move is valid.
     */
    boolean canInsertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * This method inserts the items selected by the current player in its bookshelf.
     * @param column the column to insert in.
     * @param order the list representing a permutation of the items.
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
     * @param nickname the {@code Player}'s name.
     * @return the personal score of a {@code Player}.
     */
    int getPersonalScore(String nickname);

    /**
     * This method returns the nickname of the current {@code Player}.
     * @return It returns the nickname of {@code currentPlayer}.
     */
    String getCurrentPlayer();

    void setEndingTokenListener(EndingTokenListener listener);

    void setScoreListener(ScoreListener listener);

    void setBookshelfListener(BookshelfListener listener);

    void setCommonGoalCardsListener(CommonGoalCardsListener listener);

    void setLivingRoomListener(LivingRoomListener listener);

    void setPersonalGoalCardListener(PersonalGoalCardListener listener);

    void setItemsChosenListener(ItemsChosenListener listener);
}
