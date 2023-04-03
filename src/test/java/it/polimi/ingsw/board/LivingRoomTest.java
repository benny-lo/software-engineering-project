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
    private void fill(Bag bag, LivingRoom livingRoom){
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if (bag.isEmpty()) return;
                if(livingRoom.tileAt(i, j) == null){
                    livingRoom.setTile(bag.extract(), new Position(i, j));
                }
            }
        }
    }
    @Test
    public void constructorTest(){
        LivingRoom grid=new LivingRoom(2);
        grid.printLivingRoom();
        System.out.println("\n");
        grid=new LivingRoom(3);
        grid.printLivingRoom();
        System.out.println("\n");
        grid=new LivingRoom(4);
        grid.printLivingRoom();
    }


    @Test
    public void selectableTest(){
        LivingRoom grid = new LivingRoom(2);
        Bag bag = new Bag(22);
        fill(bag, grid);

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
        LivingRoom livingRoom = new LivingRoom(2);
        assertTrue(livingRoom.isRefillNeeded());
    }

    /**
     * Check if refill is needed on full living room for 2 players.
     */
    @Test
    public void checkRefillOnFullLivingRoom2Players() {
        LivingRoom livingRoom = new LivingRoom(2);
        fill(new Bag(22), livingRoom);
        assertFalse(livingRoom.isRefillNeeded());
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
}
