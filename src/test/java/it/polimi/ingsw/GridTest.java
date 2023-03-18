package it.polimi.ingsw;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Grid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Unit tests for Grid
 */

public class GridTest {
    @Test
    public void testGrid() {
        Bag bag = new Bag(22);
        Grid grid = new Grid(3,bag);
        grid.printGrid();
    }
}
