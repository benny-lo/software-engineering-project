package it.polimi.ingsw.playerTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.player.Bookshelf;
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

//    @Test
//    public void testUpdatePersonalScore(){
//        Item item = Item.CAT;
//        Position position = new Position(0,0);
//        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<Position, Item>(Map.of(position, item)));
//        Player player = new Player("Nickname", pattern);
//
//        //player.updatePersonalScore();
//        //assertEquals(0, player.getPersonalScore());
//
//        player.insertTiles(new LinkedList<Item>(List.of(Item.CAT)), 0);
//        player.updatePersonalScore();
//        assertEquals(1, player.getPersonalScore());
//    }

    @Test
    public void testNonDovrebbeEssereQuiMaPersonalGoalPattern(){
        Bookshelf bookshelf = new Bookshelf(6,5);
        Item item = Item.CAT;
        Position position = new Position(1,1);
        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position, item)));

        assertEquals(0,pattern.check(bookshelf));

        bookshelf.insert(item, 1);
        bookshelf.insert(item, 1);
        assertEquals(2, pattern.check(bookshelf));
    }
}
