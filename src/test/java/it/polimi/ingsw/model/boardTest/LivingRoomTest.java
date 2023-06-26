package it.polimi.ingsw.model.boardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.LivingRoom;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for LivingRoom
 */

public class LivingRoomTest {
    private void fill(Bag bag, LivingRoom livingRoom) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (bag.isEmpty()) return;
                if (livingRoom.tileAt(i, j) == null) {
                    livingRoom.setTile(bag.extract(), new Position(i, j));
                }
            }
        }
    }

    /**
     * Test {@code LivingRoom}'s constructor, it is not null.
     */
    @Test
    public void testLivingRoomConstructor(){
        LivingRoom livingRoom = new LivingRoom(4);

        assertNotNull(livingRoom);
    }

    /**
     * Test {@code LivingRoom}'s method {@code setTile}, setting an {@code Item} on a null tile.
     */
    @Test
    public void testSetTileOnNull(){
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(5, 5));
        assertEquals(Item.CAT, livingRoom.tileAt(5, 5));
    }

    /**
     * Test {@code LivingRoom}'s method {@code setTile}, setting an {@code Item} on an occupied tile.
     */
    @Test
    public void testSetTileOnAnOccupiedTile(){
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(5, 5));
        assertEquals(Item.CAT, livingRoom.tileAt(5, 5));

        livingRoom.setTile(Item.BOOK, new Position(5, 5));
        assertEquals(Item.CAT, livingRoom.tileAt(5, 5));
        assertNotEquals(Item.BOOK, livingRoom.tileAt(5, 5));
    }

    /**
     * Test {@code LivingRoom}'s method {@code setTile}, setting an {@code Item} on a locked tile.
     */
    @Test
    public void testSetTileOnAnLockedTile(){
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(0, 0));
        assertEquals(Item.LOCKED, livingRoom.tileAt(0, 0));
        assertNotEquals(Item.CAT, livingRoom.tileAt(5, 5));
    }

    /**
     * Check if refill is needed on empty living room.
     */
    @Test
    public void testCheckRefillOnEmptyLivingRoom() {
        LivingRoom livingRoom = new LivingRoom(2);
        assertTrue(livingRoom.isRefillNeeded());
    }

    /**
     * Check if refill is needed on full living room for 2 players.
     */
    @Test
    public void testCheckRefillOnFullLivingRoom2Players() {
        LivingRoom livingRoom = new LivingRoom(2);
        fill(new Bag(22), livingRoom);
        assertFalse(livingRoom.isRefillNeeded());
    }

    /**
     * Test {@code LivingRoom}'s method {@code tileAt} on a locked {@code Item}.
     */
    @Test
    public void testTileAtOnLockedTile(){
        LivingRoom livingRoom = new LivingRoom(2);

        assertEquals(Item.LOCKED, livingRoom.tileAt(0,0));
    }

    /**
     * Test {@code LivingRoom}'s method {@code tileAt} on null.
     */
    @Test
    public void testTileAtOnNull(){
        LivingRoom livingRoom = new LivingRoom(2);

        assertNull(livingRoom.tileAt(5,5));
    }

    /**
     * Test {@code LivingRoom}'s method {@code tileAt} on an occupied tile with an {@code Item}.
     */
    @Test
    public void testTileAtOnOccupiedTile(){
        LivingRoom livingRoom = new LivingRoom(2);
        Bag bag = new Bag(22);
        fill(bag, livingRoom);

        assertNotEquals(Item.LOCKED, livingRoom.tileAt(5,5));
        assertNotNull(livingRoom.tileAt(5,5));
    }


    /**
     * Test removal of an item from living room.
     */
    @Test
    public void testRemovalSingleItem() {
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(2, 3));
        assertTrue(livingRoom.selectable(2, 3));

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(2, 3));

        Item item = livingRoom.tileAt(2, 3);
        assertEquals(livingRoom.selectTiles(positions), List.of(item));
        assertNull(livingRoom.tileAt(2, 3));
    }

    /**
     * Test {@code LivingRoom}'s method {@code selectable} on null.
     */
    @Test
    public void testSelectableOnNull(){
        LivingRoom livingRoom = new LivingRoom(2);

        assertFalse(livingRoom.selectable(5, 5));
    }

    /**
     * Test {@code LivingRoom}'s method {@code selectable} on a locked {@code Item}.
     */
    @Test
    public void testSelectableOnLockedTile(){
        LivingRoom livingRoom = new LivingRoom(2);

        assertFalse(livingRoom.selectable(0, 0));
    }

    /**
     * Test {@code LivingRoom}'s method {@code selectable} on an {@code Item} without free sides.
     */
    @Test
    public void testSelectableOnItemWithoutFreeSides(){
        LivingRoom livingRoom = new LivingRoom(2);
        Bag bag = new Bag(22);
        fill(bag, livingRoom);

        assertFalse(livingRoom.selectable(5, 5));
    }

    /**
     *Test {@code LivingRoom}'s method {@code selectable} with an {@code Item} with a free side.
     */
    @Test
    public void testSelectableItemWithAFreeSide(){
        LivingRoom livingRoom = new LivingRoom(2);
        Bag bag = new Bag(22);
        fill(bag, livingRoom);

        assertTrue(livingRoom.selectable(2, 3));
    }

    /**
     *Test {@code LivingRoom}'s method {@code selectable} with an {@code Item} with 2 free sides.
     */
    @Test
    public void testSelectableItemWith2FreeSides(){
        LivingRoom livingRoom = new LivingRoom(2);
        Bag bag = new Bag(22);
        fill(bag, livingRoom);

        assertTrue(livingRoom.selectable(1, 4));
    }

    /**
     *Test {@code LivingRoom}'s method {@code selectable} with an {@code Item} with 3 free sides.
     */
    @Test
    public void testSelectableItemWith3FreeSides(){
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(1, 4));
        livingRoom.setTile(Item.CAT, new Position(2, 4));

        assertTrue(livingRoom.selectable(1, 4));
    }

    /**
     *Test {@code LivingRoom}'s method {@code selectable} with an {@code Item} with 4 free sides.
     */
    @Test
    public void testSelectableItemWith4FreeSides(){
        LivingRoom livingRoom = new LivingRoom(2);

        livingRoom.setTile(Item.CAT, new Position(2, 5));

        assertTrue(livingRoom.selectable(2, 5));
    }
}
