package it.polimi.ingsw.view.client.cli;

/**
 * Enumeration of the {@code CLInterface} statuses.
 */
public enum CLIStatus {
    LOGIN, //you can only log in
    LOBBY, //you can create or select a game
    GAME, //you can select or insert tiles, and you can chat
    ENDED_GAME, //you can only exit
    ERROR, // network error
    CHAT  // the user is in chat and can send broadcast/unicast messages.
}
