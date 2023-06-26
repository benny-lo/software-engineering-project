package it.polimi.ingsw.view;

import it.polimi.ingsw.utils.message.client.*;

/**
 * Interface receiving inputs from CLI/GUI or over the network.
 */
public interface InputViewInterface {
    /**
     * The client chooses their nickname.
     * @param message Message containing the chosen nickname.
     */
    void login(Nickname message);

    /**
     * The client decides to create a new game.
     * @param message Message containing the information about the game to create.
     */
    void createGame(GameInitialization message);

    /**
     * The client selects an already existing, and not yet started, game.
     * @param message Message containing the id of the game chosen.
     */
    void selectGame(GameSelection message);

    /**
     * The client selects positions from the {@code LivingRoom} in the game they are in.
     * @param message Message containing the chosen positions.
     */
    void selectFromLivingRoom(LivingRoomSelection message);

    /**
     * The client decides where to insert the chosen tiles in their {@code Bookshelf}.
     * @param message Message containing the column and the order in which to insert the chosen tiles.
     */
    void insertInBookshelf(BookshelfInsertion message);

    /**
     * The client writes a new chat message.
     * @param message Message containing the text written.
     */
    void writeChat(ChatMessage message);
}
