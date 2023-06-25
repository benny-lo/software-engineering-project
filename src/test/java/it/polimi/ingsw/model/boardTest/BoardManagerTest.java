package it.polimi.ingsw.model.boardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.model.board.BoardManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for BoardManager.
 */
public class BoardManagerTest {
    /**
     * Test {@code BoardManager}'s constructor.
     */
    @Test
    public void testBoardManagerConstructor() {
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        assertTrue(boardManager.isEndingToken());
    }

    /**
     *  Test {@code BoardManager}'s method {@code fill} on a null tile.
     */
    @Test
    public void testFillOnNull(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        assertNull(boardManager.getLivingRoom().tileAt(5, 5));

        boardManager.fill();

        assertNotNull(boardManager.getLivingRoom().tileAt(5, 5));
        assertNotEquals(Item.LOCKED, boardManager.getLivingRoom().tileAt(5, 5));
    }

    /**
     *  Test {@code BoardManager}'s method {@code fill} on a locked {@code Item}.
     */
    @Test
    public void testFillOnLockedTile(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        assertEquals(Item.LOCKED, boardManager.getLivingRoom().tileAt(0, 0));

        boardManager.fill();

        assertNotNull(boardManager.getLivingRoom().tileAt(0, 0));
        assertEquals(Item.LOCKED, boardManager.getLivingRoom().tileAt(0, 0));
    }

    /**
     *  Test {@code BoardManager}'s method {@code fill} on an already occupied {@code Item}.
     */
    @Test
    public void testFillOnOccupiedTile(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertNotEquals(Item.LOCKED, boardManager.getLivingRoom().tileAt(5, 5));
        assertNotNull(boardManager.getLivingRoom().tileAt(5, 5));

        Item item = boardManager.getLivingRoom().tileAt(5, 5);

        boardManager.fill();

        assertEquals(item, boardManager.getLivingRoom().tileAt(5, 5));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} without {@code Item}s.
     */
    @Test
    public void testCanTakeItemTilesBoardWithoutItems(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertFalse(boardManager.canTakeItemTilesBoard(new ArrayList<>()));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} with 4 {@code Item}s.
     */
    @Test
    public void testCanTakeItemTilesBoardWith4Items(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertFalse(boardManager.canTakeItemTilesBoard(new ArrayList<>(List.of(new Position(3, 2), new Position(3, 3), new Position(3, 4), new Position(3, 5)))));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} with 3 not selectable {@code Item}s.
     */
    @Test
    public void testCanTakeItemTilesBoardWith3NotSelectableItems(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertFalse(boardManager.canTakeItemTilesBoard(new ArrayList<>(List.of(new Position(3, 2), new Position(3, 3), new Position(3, 4)))));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} with 2 {@code Item}s placed diagonally.
     */
    @Test
    public void testCanTakeItemTilesBoardDiagonal(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertFalse(boardManager.canTakeItemTilesBoard(new ArrayList<>(List.of(new Position(3, 2), new Position(2, 3)))));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} with 2 selectable {@code Item}s placed horizontally.
     */
    @Test
    public void testCanTakeItemTilesBoardHorizontal(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertTrue(boardManager.getLivingRoom().selectable(new Position(1, 3)));
        assertTrue(boardManager.getLivingRoom().selectable(new Position(1, 4)));

        assertTrue(boardManager.canTakeItemTilesBoard(new ArrayList<>(List.of(new Position(1, 3), new Position(1, 4)))));
    }

    /**
     * Test {@code BoardManager}'s method {@code canTakeItemTilesBoard} with 2 selectable {@code Item}s placed vertically.
     */
    @Test
    public void testCanTakeItemTilesBoardVertical(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.fill();

        assertTrue(boardManager.getLivingRoom().selectable(new Position(1, 3)));
        assertTrue(boardManager.getLivingRoom().selectable(new Position(1, 4)));

        assertTrue(boardManager.canTakeItemTilesBoard(new ArrayList<>(List.of(new Position(4, 1), new Position(5, 1)))));
    }

    /**
     * Test {@code BoardManager}'s method {@code selectItemTiles}, selecting one {@code Item}.
     */
    @Test
    public void testSelectItemTilesOneItem(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.getLivingRoom().setTile(Item.CAT, new Position(2, 3));
        assertTrue(boardManager.getLivingRoom().selectable(2, 3));

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(2, 3));

        Item item = boardManager.getLivingRoom().tileAt(2, 3);
        assertEquals(boardManager.selectItemTiles(positions), List.of(item));
        assertNull(boardManager.getLivingRoom().tileAt(2, 3));
    }

    /**
     * Test {@code BoardManager}'s method {@code isEndingToken} and {@code takeEndingToken}.
     */
    @Test
    public void testEndingToken(){
        BoardManager boardManager = null;
        try {
            boardManager = new BoardManager(2);
        } catch (IOException e) {
            fail();
        }

        boardManager.takeEndingToken();
        assertFalse(boardManager.isEndingToken());
    }
}
