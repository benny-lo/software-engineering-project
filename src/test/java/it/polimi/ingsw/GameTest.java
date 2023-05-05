package it.polimi.ingsw;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
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
        Game game = new Game(2);

        assertNotNull(game);
        assertEquals(0, game.getNumberPlayers());
        assertNull(game.getCurrentPlayer());
        assertNotNull(game.getPlayers());
    }

    /**
     * Test {@code Game}'s method {@code addPlayer}, adding a player.
     */
    @Test
    public void testAddPlayer(){
        Game game = new Game(2);
        String nickname = "nickname";

        game.addPlayer(nickname);

        assertEquals(1, game.getNumberPlayers());
        assertTrue(game.getPlayers().containsKey("nickname"));
        assertNotNull(game.getPlayers().get(nickname));
    }

    /**
     * Test {@code Game}'s method {@code setup}, it initializes the {@code CommonGoalCardManager} and the {@code BoardManager}.
     */
    @Test
    public void testSetup(){
        Game game = new Game(2);

        game.addPlayer("0");
        game.addPlayer("1");
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
        Game game = new Game(2);

        game.setCurrentPlayer("nickname");

        assertEquals("nickname", game.getCurrentPlayer());
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} on a wrong tile.
     */
    @Test
    public void testCanTakeItemTilesOnWrongTile(){
        Game game = new Game(2);

        game.addPlayer("0");
        game.addPlayer("1");
        game.setup();

        assertFalse(game.canTakeItemTiles(List.of(new Position(0, 0))));
    }

    /**
     * Test {@code Game}'s method {@code canTakeItemTiles} without enough space in the bookshelf.
     */
    @Test
    public void testCanTakeItemTilesWithoutEnoughSpace(){
        Game game = new Game(2);

        game.addPlayer("nickname");
        game.addPlayer("1");

        game.setup();

        game.getPlayers().get("nickname").getBookshelf().insert(List.of(Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT, Item.CAT), 0);

        assertFalse(game.canTakeItemTiles(List.of(new Position(5, 5))));
    }
}
