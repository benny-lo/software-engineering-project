package it.polimi.ingsw.playerTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import org.junit.jupiter.api.Test;

import java.util.*;


/**
 * Unit tests of {@code Player}. WIP
 */

public class PlayerTest {
    /**
     * Test {@code Player} constructor.
     */
    @Test
    public void testPlayerConstructor(){
        Item item = Item.CAT;
        Position position = new Position(1,0);
        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position, item)));
        Player player = new Player("Nickname", pattern);

        assertNotNull(player);
        assertNotNull(player.getBookshelf());
        assertNotNull(player.getPersonalGoalCard());
        assertEquals(0, player.getPersonalScore());
        assertEquals(0, player.getBookshelfScore());
    }
}
