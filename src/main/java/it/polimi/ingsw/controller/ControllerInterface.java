package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.List;

/**
 * Interface used by {@code Lobby} and the {@code VirtualView}s to interact with a match.
 */
public interface ControllerInterface {
    /**
     * Joins a {@code ServerUpdateViewInterface} into the match. If the join is rejected, the client will be notified.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    void join(ServerUpdateViewInterface view);

    /**
     * Performs a selection from the {@code LivingRoom}. If the selection is rejected, the client will be notified.
     * @param positions {@code List} of {@code Position}s that are chosen by the player.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    void livingRoom(List<Position> positions, ServerUpdateViewInterface view);

    /**
     * Performs an insertion in the player's {@code Bookshelf}. If the insertion is rejected, the client will be notified.
     * @param column The column of the {@code Bookshelf} where to insert the previously chosen {@code Item}s
     * @param permutation {@code List} of {@code Integer}s representing the order in which to insert the {@code Item}s in the {@code Bookshelf}.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    void bookshelf(int column, List<Integer> permutation, ServerUpdateViewInterface view);

    /**
     * Processes a chat message from a player. If the message is rejected, the client will be notified.
     * @param text The content of the message.
     * @param receiver The receiver of the message.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     */
    void chat(String text, String receiver, ServerUpdateViewInterface view);

    /**
     * Processes a disconnection of a client.
     * @param view The {@code ServerUpdateViewInterface} that disconnected.
     */
    void disconnection(ServerUpdateViewInterface view);

    /**
     * Returns whether the game has started.
     * @return {@code true} iff the game has already started.
     */
    boolean isStarted();

    /**
     * Getter for the number of players.
     * @return The number of players.
     */
    int getNumberPlayers();

    /**
     * Getter for the number of common goal cards.
     * @return The number of common goal cards.
     */
    int getNumberCommonGoalCards();

    /**
     * Getter for the number of currently connected players.
     * @return The number of currently connected players.
     */
    int getNumberActualPlayers();
}
