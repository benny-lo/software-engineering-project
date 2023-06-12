package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.Position;
import org.junit.jupiter.api.Test;

import java.util.Objects;


public class PositionTest {
    /**
     * Test for the {@code Position}'s constructor with row and column
     */
    @Test
    public void testConstructor()
    {
        Position one = new Position(1,2);

        assertEquals(1, one.getRow());
        assertEquals(2, one.getColumn());
    }
    /**
     * Test for the {@code Position}'s constructor with another position
     */
    @Test
    public void testConstructor2()
    {
        Position one = new Position(1,2);
        Position two = new Position(one);

        assertEquals(1, two.getRow());
        assertEquals(2, two.getColumn());
    }
    /**
     * Test for the json constructor
     */
    @Test
    public void testJson()
    {
        Position json = new Position();
        assertEquals(-1, json.getColumn());
        assertEquals(-1, json.getRow());
    }
    /**
    * Test for the override of the equals method
    */
    @Test
    public void testEquals()
    {
        Position one = new Position(1,2);
        Object obj = new Object();
        Position two = new Position(1,2);
        Position three = new Position(0,0);
        assertEquals(one, one);
        assertEquals(one, two);
        assertNotEquals(one, three);
        assertNotEquals(one, obj);
    }

    /**
     * Test for the override of hashCode method
     */
    @Test
    public void testHashCode()
    {
        Position one = new Position(1,1);
        int obj = one.hashCode();
        int obj1 = Objects.hash(1,1);
        assertEquals(obj,obj1);
    }
    /**
     * Tests for override of compareTo method
     */
    @Test
    public void testCompareTo()
    {
        Position one = new Position(1,1);
        Position two = new Position(1,1);
        Position three = new Position(5,1);
        Position four = new Position(1,10);
        assertEquals(0, one.compareTo(two));
        assertEquals(-4, one.compareTo(three));
        assertEquals(-9, one.compareTo(four));
    }

}
