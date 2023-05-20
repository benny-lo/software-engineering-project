package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.message.client.*;

/**
 * Interfaces to implement by classes that need to receive action from a client.
 */
public interface InputViewInterface {
    /**
     * The client chooses their nickname.
     * @param message message containing the chosen nickname.
     */
    void login(Nickname message);

    /**
     * The client decides to create a new game.
     * @param message message containing the information about the game to create.
     */
    void createGame(GameInitialization message);

    /**
     * The client selects an already existing, and not yet started, game.
     * @param message message containing the id of the game chosen.
     */
    void selectGame(GameSelection message);

    /**
     * The client selects positions from the {@code LivingRoom} in the game they are in.
     * @param message message containing the chosen positions.
     */
    void selectFromLivingRoom(LivingRoomSelection message);

    /**
     * The client decides where to insert the chosen tiles in their {@code Bookshelf}.
     * @param message message containing the column and the order in which to insert the chosen tiles.
     */
    void insertInBookshelf(BookshelfInsertion message);

    /**
     * The client writes a new chat message.
     * @param message message containing the text written.
     */
    void writeChat(ChatMessage message);
}
