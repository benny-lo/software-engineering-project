package it.polimi.ingsw.model;

import java.util.List;

/**
 * Interface representing a {@code Game}.
 */

public interface GameInterface {
    /**
     * It creates a {@code Player}, that is added to {@code Game}.
     * @param nickname {@code nickname} is the {@code Player}'s name.
     */
    void addPlayer(String nickname);

    /**
     * This method starts the game, and it initializes a {@code PersonalGoalCard} for each {@code Player}.
     */
    void setup();

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
     * This method inserts the items selected by the current player in its bookshelf.
     * @param column the column to insert in.
     * @param order the list representing a permutation of the items.
     */
    void insertItemTilesInBookshelf(int column, List<Integer> order);

    /**
     * This method compares the {@code totalScore} of the {@code Player}s, and the {@code Player} with the highest {@code totalScore} is the winner.
     * If there is a tie between 2 or more {@code Player}s, the {@code Player} with the {@code endingToken} is the winner.
     * @return It returns the winner's name.
     */
    String getWinner();

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
}
