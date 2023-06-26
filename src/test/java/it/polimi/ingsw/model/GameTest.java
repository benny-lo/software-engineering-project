package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.*;

/**
 * Unit tests for Game.
 */
public class GameTest {
    /**
     * Test {@code Game}'s constructor, game and the current player aren't null.
     */
    @Test
    public void testGameConstructor() {
        Game game = null;
        try {
            game = new Game(List.of("nick", "rick"), 2);
        } catch (IOException e) {
            fail();
        }

        assertNotNull(game);
        assertEquals(2, game.getNumberPlayers());
        assertNull(game.getCurrentPlayer());
        assertNotNull(game.getPlayers());
    }

    /**
     * Test {@code Game}'s method {@code setup}, it initializes the {@code CommonGoalCardManager} and the {@code BoardManager}.
     */
    @Test
    public void testSetup() {
        Game game = null;
        try {
            game = new Game(List.of("nick", "rick"), 2);
        } catch (IOException e) {
            fail();
        }

        assertNotNull(game.getCommonGoalCardManager());
        assertNotNull(game.getBoardManager());
    }

    /**
     * Test {@code Game}'s method {@code setCurrentPlayer}.
     */
    @Test
    public void testSetCurrentPlayer(){
        Game game = null;
        try {
            game = new Game(List.of("nick", "rick"), 2);
        } catch (IOException e) {
            fail();
        }

        game.setCurrentPlayer("nickname");

        assertEquals("nickname", game.getCurrentPlayer());
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} on a wrong tile.
     */
    @Test
    public void testCanTakeItemTilesOnWrongTile(){
        Game game = null;
        try {
            game = new Game(List.of("nick", "rick"), 2);
        } catch (IOException e) {
            fail();
        }

        assertFalse(game.canTakeItemTiles(List.of(new Position(0, 0))));
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} without enough space in the bookshelf.
     */
    @Test
    public void testCanTakeItemTilesWithoutEnoughSpace(){
        Game game = null;
        try {
            game = new Game(List.of("nick", "rick"), 2);
        } catch (IOException e) {
            fail();
        }

        game.getPlayers().get("nick").getBookshelf().insert(List.of(Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT), 0);

        assertFalse(game.canTakeItemTiles(List.of(new Position(5, 5))));
    }
}
