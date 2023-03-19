package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.LivingRoom;
import org.junit.jupiter.api.Test;

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
        Bag bag= new Bag(22);
        LivingRoom grid=new LivingRoom(2, bag);
        System.out.println(grid.selectable(0,3));
        System.out.println(grid.selectable(1,3));
        System.out.println(grid.selectable(2,3));
        System.out.println(grid.selectable(3,3));
        System.out.println(grid.selectable(4,3));
    }

}
