package it.polimi.ingsw.controller;

/**
 * Enumeration representing the only two possible match states, when the game has been started.
 */
public enum TurnPhase {
    /**
     * The current player has to select {@code Item}s from the {@code LivingRoom}.
     */
    LIVING_ROOM,

    /**
     * The current player has to insert the selected {@code Item}s in the {@code Bookshelf}.
     */
    BOOKSHELF
}
