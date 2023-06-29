package it.polimi.ingsw.utils;

/**
 * Enumeration of all possible types of item tiles. They are part of the {@code LivingRoom} and of the {@code Bookshelf}.
 * This enumeration is also used by other the view package and the controller package.
 */
public enum Item {
    /**
     * Item of the game: the color is green.
     */
    CAT,

    /**
     * Item of the game: the color is white.
     */
    BOOK,

    /**
     * Item of the game: the color is yellow.
     */
    GAME,

    /**
     * Item of the game: the color is dark blue.
     */
    FRAME,

    /**
     * Item of the game: the color is light blue.
     */
    CUP,

    /**
     * Item of the game: the color is purple.
     */
    PLANT,

    /**
     * Used in the {@code LivingRoom} to represent an empty position where no
     * {@code Item} can be placed.
     */
    LOCKED
}
