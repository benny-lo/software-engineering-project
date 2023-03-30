package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.BoardManager;
import it.polimi.ingsw.model.board.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Player;

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
     *  Da scrivere
     * @param column
     * @param order
     */
    void insertItemTilesInBookshelf(int column, int[] order);

    /**
     * This method compares the {@code totalScore} of the {@code Player}s, and the {@code Player} with the highest {@code totalScore} is the winner.
     * If there is a tie between 2 or more {@code Player}s, the {@code Player} with the {@code endingToken} is the winner.
     * @return It returns the winner's name.
     */
    String getWinner();

    /**
     * This method gets the {@code Player}'s {@code totalScore}.
     * @param nickname It's the {@code Players}'s name.
     * @return It returns the {@code totalScore} of a {@code Player}.
     */
    int getScore(String nickname);

    /**
     * This method gets the {@code Player} with this {@code nickname}.
     * @param nickname It's the {@code Players}'s name.
     * @return It returns the {@code Player} with this {@code nickname}.
     */
    Player getPlayer(String nickname);

    /**
     * This method returns the number of players in the {@code Game}.
     * @return It returns {@code numberPlayers}.
     */
    int getNumberPlayers();

    /**
     * This method returns the number of {@code CommonGoalCard}s in the {@code Game}.
     * @return It returns {@code numberGoalCards}.
     */
    int getNumberGoalCards();

    /**
     * This method returns the current {@code Player}.
     * @return It returns {@code currentPlayer}.
     */
    Player getCurrentPlayer();

    /**
     * This method returns the current {@code BoardManager}.
     * @return It returns {@code BoardManager}.
     */
    BoardManager getBoardManager();

    /**
     * This method returns the current {@code CommonGoalCardManager}.
     * @return It returns {@code CommonGoalCardManager}.
     */
    CommonGoalCardManager getCommonGoalCardManager();
}
