package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.List;

/**
 * Interface used by {@code Lobby} and the {@code VirtualView}s to interact with a match.
 */
public interface ControllerInterface {
    /**
     * Joins a {@code ServerUpdateViewInterface} into the match. If the join is rejected, the client will be notified.
     * @param view The {@code ServerUpdateViewInterface} performing the action.
     * @param nickname The nickname of the player performing the action.
     * @return {@code true} iff the join completed successfully.
     */
    boolean join(ServerUpdateViewInterface view, String nickname);

    /**
     * Performs a selection from the {@code LivingRoom}. If the selection is rejected, the client will be notified.
     * @param positions {@code List} of {@code Position}s that are chosen by the player.
     * @param nickname The nickname of the player performing the action.
     */
    void livingRoom(List<Position> positions, String nickname);

    /**
     * Performs an insertion in the player's {@code Bookshelf}. If the insertion is rejected, the client will be notified.
     * @param column The column of the {@code Bookshelf} where to insert the previously chosen {@code Item}s
     * @param permutation {@code List} of {@code Integer}s representing the order in which to insert the {@code Item}s in the {@code Bookshelf}.
     * @param nickname The nickname of the player performing the action.
     */
    void bookshelf(int column, List<Integer> permutation, String nickname);

    /**
     * Processes a chat message from a player. If the message is rejected, the client will be notified.
     * @param text The content of the message.
     * @param receiver The receiver of the message.
     * @param nickname The nickname of the player performing the action.
     */
    void chat(String text, String receiver, String nickname);

    /**
     * Processes a disconnection of a client.
     * @param nickname The nickname of the player performing the action.
     */
    void disconnection(String nickname);


    /**
     * Processes a reconnection of a previously disconnected player.
     * @param view The view of the player reconnecting.
     * @param nickname The nickname of the client reconnecting.
     * @return {@code true} iff the reconnection went fine.
     */
    boolean reconnection(ServerUpdateViewInterface view, String nickname);

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
     * Returns whether the game has ended.
     * @return {@code true} iff the game has ended.
     */
    boolean isEnded();
}
