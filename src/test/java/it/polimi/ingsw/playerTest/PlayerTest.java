package it.polimi.ingsw.playerTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;
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
        assertNull(player.getScoringToken(0));
        assertNull(player.getScoringToken(1));
        assertNull(player.getEndingToken());
    }

    /**
     * Test {@code Player}'s method {@code updatePersonalScore} without a match.
     */
    @Test
    public void testUpdatePersonalScoreWithoutMatches(){
        Item item = Item.CAT;

        Position position = new Position(1,0);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position, item)));
        Player player = new Player("Nickname", pattern);

        player.insertTiles(List.of(item), 0);
        player.insertTiles(List.of(item, item, item), 1);

        player.updatePersonalScore();
        assertEquals(0, player.getPersonalScore());
    }

    /**
     * Test {@code Player}'s method {@code updatePersonalScore} with 4 matches.
     */
    @Test
    public void testUpdatePersonalScoreWith4Matches(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        Position position_1 = new Position(1,0);
        Position position_2 = new Position(2,1);
        Position position_3 = new Position(1,4);
        Position position_4 = new Position(0,2);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position_1, item_c, position_2, item_b, position_3, item_f, position_4, item_p)));
        Player player = new Player("Nickname", pattern);

        player.insertTiles(List.of(item_b, item_c,item_c), 0);
        player.insertTiles(List.of(item_f), 0);
        player.insertTiles(List.of(item_b, item_c,item_b), 1);
        player.insertTiles(List.of(item_b, item_f,item_c), 4);
        player.insertTiles(List.of(item_p, item_c,item_c), 2);
        player.insertTiles(List.of(item_c), 2);

        player.updatePersonalScore();
        assertEquals(6, player.getPersonalScore());
    }

    /**
     * Test {@code Player}'s method {@code updateBookshelfScore} without an island.
     */
    @Test
    public void testUpdateBookshelfScoreWithoutIslands(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.insertTiles(List.of(item_b, item_c,item_c, item_f), 0);
        player.insertTiles(List.of(item_f, item_f,item_b), 1);
        player.insertTiles(List.of(item_b, item_c,item_b,item_c), 2);
        player.insertTiles(List.of(item_p, item_f,item_c), 3);

        player.updateBookshelfScore();
        assertEquals(0, player.getBookshelfScore());
    }

    /**
     * Test {@code Player}'s method {@code updateBookshelfScore} with 2 islands.
     */
    @Test
    public void testUpdateBookshelfScoreWith2Islands(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.insertTiles(List.of(item_b, item_c,item_c), 0);
        player.insertTiles(List.of(item_f), 0);
        player.insertTiles(List.of(item_b, item_b,item_b), 1);
        player.insertTiles(List.of(item_b, item_c,item_c), 2);
        player.insertTiles(List.of(item_c), 2);
        player.insertTiles(List.of(item_p, item_f,item_c), 3);

        player.updateBookshelfScore();
        assertEquals(5+3, player.getBookshelfScore());
    }

    /**
     * Test {@code Player}'s method {@code addScoringToken} with 2 tokens of the same type.
     */
    @Test
    public void testAddScoringTokenWithSameType(){
        ScoringToken token = new ScoringToken(4, 0);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.addScoringToken(token);

        assertEquals(token, player.getScoringToken(0));

        player.addScoringToken(token);

        assertEquals(token, player.getScoringToken(0));
        assertNull(player.getScoringToken(1));
    }

    /**
     * Test {@code Player}'s method {@code addScoringToken} with 3 tokens.
     */
    @Test
    public void testAddScoringTokenWith3Tokens(){
        ScoringToken token_0 = new ScoringToken(4, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        ScoringToken token_2 = new ScoringToken(4, 2);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.addScoringToken(token_0);

        assertEquals(token_0, player.getScoringToken(0));

        player.addScoringToken(token_1);

        assertEquals(token_0, player.getScoringToken(0));
        assertEquals(token_1, player.getScoringToken(1));

        player.addScoringToken(token_2);

        assertEquals(token_0, player.getScoringToken(0));
        assertEquals(token_1, player.getScoringToken(1));
        assertNull(player.getScoringToken(2));
    }

    /**
     * Test {@code Player}'s method {@code addScoringToken} with an ending token.
     */
    @Test
    public void testAddScoringTokenWithEndingToken(){
        ScoringToken token_0 = new ScoringToken(4, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        ScoringToken ending_token = new ScoringToken(1, -1);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.addScoringToken(token_0);
        player.addScoringToken(ending_token);
        player.addScoringToken(token_1);

        assertEquals(token_0, player.getScoringToken(0));
        assertEquals(token_1, player.getScoringToken(1));
        assertEquals(ending_token, player.getEndingToken());
    }

    /**
     * Test {@code Player}'s method {@code addScoringToken} with 2 ending tokens.
     */
    @Test
    public void testAddScoringTokenWith2EndingToken(){
        ScoringToken ending_token_0 = new ScoringToken(1, -1);
        ScoringToken ending_token_1 = new ScoringToken(1, -1);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.addScoringToken(ending_token_0);

        assertEquals(ending_token_0, player.getEndingToken());

        player.addScoringToken(ending_token_1);

        assertEquals(ending_token_0, player.getEndingToken());
        assertNull(player.getScoringToken(0));
        assertNull(player.getScoringToken(1));
    }

    /**
     * Test {@code Player}'s method {@code insertTiles} without tiles.
     */
    @Test
    public void testInsertTilesWithoutItems(){
        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.insertTiles(new ArrayList<>(List.of()), 0);

        assertNull(player.getBookshelf().tileAt(0,0));
    }

    /**
     * Test {@code Player}'s method {@code insertTiles} with 3 items.
     */
    @Test
    public void testInsertTilesWith3Items(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.insertTiles(new ArrayList<>(List.of(item_b, item_c, item_f)), 0);

        assertEquals(item_b, player.getBookshelf().tileAt(0,0));
        assertEquals(item_c, player.getBookshelf().tileAt(1,0));
        assertEquals(item_f, player.getBookshelf().tileAt(2,0));
    }

    /**
     * Test {@code Player}'s method {@code insertTiles} with 4 items.
     */
    @Test
    public void testInsertTilesWith4Items(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.insertTiles(new ArrayList<>(List.of(item_b, item_c, item_f, item_p)), 0);

        assertNull(player.getBookshelf().tileAt(0,0));
    }

    /**
     * Test {@code Player}'s method {@code getPublicScore}.
     */
    @Test
    public void testGetPublicScore(){
        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        ScoringToken ending_token = new ScoringToken(1, -1);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>());
        Player player = new Player("Nickname", pattern);

        player.addScoringToken(token_0);
        player.addScoringToken(ending_token);
        player.addScoringToken(token_1);

        assertEquals(13, player.getPublicScore());
    }

    /**
     * Test {@code Player}'s method {@code getPublicScore}.
     */
    @Test
    public void testGetTotalScore(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        Position position_1 = new Position(1,0);
        Position position_2 = new Position(2,1);
        Position position_3 = new Position(0,3);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position_1, item_c, position_2, item_b, position_3, item_p)));
        Player player = new Player("Nickname", pattern);


        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        ScoringToken ending_token = new ScoringToken(1, -1);

        player.addScoringToken(token_0);
        player.addScoringToken(ending_token);
        player.addScoringToken(token_1);

        player.insertTiles(List.of(item_b, item_c,item_c), 0);
        player.insertTiles(List.of(item_f), 0);
        player.insertTiles(List.of(item_b, item_b,item_b), 1);
        player.insertTiles(List.of(item_b, item_c,item_c), 2);
        player.insertTiles(List.of(item_c), 2);
        player.insertTiles(List.of(item_p, item_f,item_c), 3);

        assertEquals(4+13+8, player.getTotalScore());
    }
}
