package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

/**
 * Unit tests for Game.
 */
public class GameTest {
    /**
     * Test {@code Game}'s constructor, game and the current player aren't null.
     */
    @Test
    public void testGameConstructor(){
        Game game = new Game(2, 2, Map.of("nick", new Player("nick")));

        assertNotNull(game);
        assertEquals(2, game.getNumberPlayers());
        assertNull(game.getCurrentPlayer());
        assertNotNull(game.getPlayers());
    }

    /**
     * Test {@code Game}'s method {@code setup}, it initializes the {@code CommonGoalCardManager} and the {@code BoardManager}.
     */
    @Test
    public void testSetup(){
        Game game = new Game(2, 2, Map.of("nick", new Player("nick"), "tick", new Player("tick")));

        game.setup();

        assertNotNull(game.getCommonGoalCardManager());
        assertNotNull(game.getBoardManager());
        //TODO: It's needed a check on PersonalCards when distributePersonalCards will be added.
    }

    /**
     * Test {@code Game}'s method {@code setCurrentPlayer}.
     */
    @Test
    public void testSetCurrentPlayer(){
        Game game = new Game(2, 2, Map.of("nick", new Player("nick"), "tick", new Player("tick")));

        game.setCurrentPlayer("nickname");

        assertEquals("nickname", game.getCurrentPlayer());
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} on a wrong tile.
     */
    @Test
    public void testCanTakeItemTilesOnWrongTile(){
        Game game = new Game(2, 2, Map.of("nick", new Player("nick"), "tick", new Player("tick")));

        game.setup();

        assertFalse(game.canTakeItemTiles(List.of(new Position(0, 0))));
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} without enough space in the bookshelf.
     */
    @Test
    public void testCanTakeItemTilesWithoutEnoughSpace(){
        Game game = new Game(2, 2, Map.of("nick", new Player("nick"), "tick", new Player("tick")));

        game.setup();

        game.getPlayers().get("nick").getBookshelf().insert(List.of(Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT), 0);

        assertFalse(game.canTakeItemTiles(List.of(new Position(5, 5))));
    }
}
