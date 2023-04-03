package it.polimi.ingsw.board;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.LivingRoom;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for LivingRoom
 */

public class LivingRoomTest {
    @Test
    public void constructorTest(){
        Bag bag= new Bag(22);
        LivingRoom grid=new LivingRoom(2, bag);
        grid.printLivingRoom();
        System.out.println("\n");
        grid=new LivingRoom(3, bag);
        grid.printLivingRoom();
        System.out.println("\n");
        grid=new LivingRoom(4, bag);
        grid.printLivingRoom();
    }

    @Test
    public void selectableTest(){
        Bag bag = new Bag(22);
        LivingRoom grid = new LivingRoom(2, bag);
        assertFalse(grid.selectable(0,3));
        assertTrue(grid.selectable(1,3));
        assertTrue(grid.selectable(2,3));
        assertFalse(grid.selectable(3,3));
        assertFalse(grid.selectable(4,3));
    }

    /**
     * Check if refill is needed on empty living room.
     */
    @Test
    public void checkRefillOnEmptyLivingRoom() {
        LivingRoom livingRoom = new LivingRoom(2, new Bag(0));
        assertTrue(livingRoom.isRefillNeeded());
    }

    /**
     * Check if refill is needed on full living room for 2 players.
     */
    @Test
    public void checkRefillOnFullLivingRoom2Players() {
        LivingRoom livingRoom = new LivingRoom(2, new Bag(22));
        assertFalse(livingRoom.isRefillNeeded());
    }

    /**
     * Test removal of an item from living room.
     */
    @Test
    public void testRemovalSingleItem() {
        LivingRoom livingRoom = new LivingRoom(2, new Bag(22));
        assertTrue(livingRoom.selectable(2, 3));
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(2, 3));

        Item item = livingRoom.tileAt(2, 3);
        assertEquals(livingRoom.selectTiles(positions), List.of(item));
        assertNull(livingRoom.tileAt(2, 3));
    }
}
