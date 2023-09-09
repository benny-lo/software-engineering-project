package it.polimi.ingsw.view.client.cli;

/**
 * Enumeration of the {@code CLInterface} statuses.
 */
public enum CLIStatus {
    /**
     * The client is in the log-in phase.
     */
    LOGIN,

    /**
     * The client can only create or select an already created game.
     */
    LOBBY,

    /**
     * The client can perform in-game moves: select items from the living room,
     * insert items in the bookshelf, and move to the chat.
     */
    GAME,

    /**
     * The client can only exit the game.
     */
    ENDED_GAME,

    /**
     * The client disconnected from the server.
     */
    ERROR,

    /**
     * The client can write messages in the chat, both unicast and broadcast.
     * The client can also move back to the game-view to play.
     */
    CHAT
}
