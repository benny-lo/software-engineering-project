package it.polimi.ingsw.model.playerTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalCard;
import it.polimi.ingsw.model.player.personalGoalCard.PersonalGoalPattern;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;


/**
 * Unit tests of {@code Player}.
 */

public class PlayerTest {
    private static final Position dummy = new Position(-1, -1);

    private static List<Pair<Position, Item>> convert(List<Item> items) {
        return items.stream().map(item -> new Pair<>(dummy, item)).toList();
    }

    /**
     * Test {@code Player}'s constructor.
     */
    @Test
    public void testPlayerConstructor(){
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        assertNotNull(player);
        assertNotNull(player.getBookshelf());
    }

    /**
     * Test {@code Player}'s method {@code updatePersonalScore} without a match.
     */
    @Test
    public void testUpdatePersonalScoreWithoutMatches(){
        Item item = Item.CAT;

        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.takeItems(convert(List.of(item, item)));

        player.insertTiles(0, List.of(1));

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
        PersonalGoalCard card = new PersonalGoalCard(0, pattern);
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.setPersonalGoalCard(card);

        player.takeItems(convert(List.of(item_b, item_c,item_c)));
        player.insertTiles(0, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_f)));
        player.insertTiles(0, List.of(0));
        player.takeItems(convert(List.of(item_b, item_c,item_b)));
        player.insertTiles(1, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_b, item_f,item_c)));
        player.insertTiles(4, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_p, item_c,item_c)));
        player.insertTiles(2, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_c)));
        player.insertTiles(2, List.of(0));

        assertEquals(6, player.getPersonalScore());
    }

    /**
     * Test {@code Player}'s method {@code insertTiles} without items.
     */
    @Test
    public void testInsertTilesWithoutItems(){
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.takeItems(List.of());
        player.insertTiles(0, List.of());

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

        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.takeItems(convert(List.of(item_b, item_c, item_f)));
        player.insertTiles(0, List.of(0, 1, 2));

        assertEquals(item_b, player.getBookshelf().tileAt(0,0));
        assertEquals(item_c, player.getBookshelf().tileAt(1,0));
        assertEquals(item_f, player.getBookshelf().tileAt(2,0));
    }


    /**
     * Test {@code Player}'s method {@code getPublicScore} with only scoring tokens.
     */
    @Test
    public void testGetPublicScoreWithOnlyScoringTokens(){
        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);

        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.addEndingToken();
        player.addScoringToken(token_0);
        player.addScoringToken(token_1);

        assertEquals(13, player.getPublicScore());
    }

    /**
     * Test {@code Player}'s method {@code getPublicScore} with only bookshelf score.
     */
    @Test
    public void testGetPublicScoreWithOnlyBookshelf(){
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.takeItems(convert(List.of(item_b, item_c,item_c)));
        player.insertTiles(0, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_f)));
        player.insertTiles(0, List.of(0));
        player.takeItems(convert(List.of(item_b, item_b,item_b)));
        player.insertTiles(1, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_p, item_c,item_c)));
        player.insertTiles(2, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_c)));
        player.insertTiles(2, List.of(0));
        player.takeItems(convert(List.of(item_b, item_f,item_c)));
        player.insertTiles(4, List.of(0, 1, 2));


        assertEquals(5, player.getPublicScore());
    }
    /**
     * Test {@code Player}'s method {@code getPublicScore} with bookshelf score and scoring tokens.
     */
    @Test
    public void testGetPublicScore(){
        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.addEndingToken();
        player.addScoringToken(token_0);
        player.addScoringToken(token_1);

        player.takeItems(convert(List.of(item_b, item_c,item_c)));
        player.insertTiles(0, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_f)));
        player.insertTiles(0, List.of(0));
        player.takeItems(convert(List.of(item_b, item_b,item_b)));
        player.insertTiles(1, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_p, item_c,item_c)));
        player.insertTiles(2, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_c)));
        player.insertTiles(2, List.of(0));
        player.takeItems(convert(List.of(item_b, item_f,item_c)));
        player.insertTiles(4, List.of(0, 1, 2));


        assertEquals(18, player.getPublicScore());
    }
    /**
     * Test {@code Player}'s method {@code getTotalScore}.
     */
    @Test
    public void testGetTotalScore(){
        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);
        Item item_c = Item.CAT;
        Item item_b = Item.BOOK;
        Item item_f = Item.FRAME;
        Item item_p = Item.PLANT;

        Position position_1 = new Position(1,0);
        Position position_2 = new Position(2,1);
        Position position_3 = new Position(1,4);
        Position position_4 = new Position(0,2);

        PersonalGoalPattern pattern = new PersonalGoalPattern(new HashMap<>(Map.of(position_1, item_c, position_2, item_b, position_3, item_f, position_4, item_p)));
        PersonalGoalCard card = new PersonalGoalCard(0, pattern);
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        player.setPersonalGoalCard(card);

        player.addEndingToken();
        player.addScoringToken(token_0);
        player.addScoringToken(token_1);

        player.takeItems(convert(List.of(item_b, item_c,item_c)));
        player.insertTiles(0, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_f)));
        player.insertTiles(0, List.of(0));
        player.takeItems(convert(List.of(item_b, item_b,item_b)));
        player.insertTiles(1, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_p, item_c,item_c)));
        player.insertTiles(2, List.of(0, 1, 2));
        player.takeItems(convert(List.of(item_c)));
        player.insertTiles(2, List.of(0));
        player.takeItems(convert(List.of(item_b, item_f,item_c)));
        player.insertTiles(4, List.of(0, 1, 2));

        assertEquals(6+13+5, player.getTotalScore());
    }

    /**
     * Test {@code Player}'s method {@code cannotTake} without tokens.
     */
    @Test
    public void testCannotTakeWithoutTokens(){
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        assertNotNull(player.cannotTake());
        assertEquals(0, player.cannotTake().size());
    }

    /**
     * Test {@code Player}'s method {@code cannotTake} with 2 tokens.
     */
    @Test
    public void testCannotTakeWith2Tokens(){
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }
        ScoringToken token_0 = new ScoringToken(8, 0);
        ScoringToken token_1 = new ScoringToken(4, 1);

        player.addScoringToken(token_0);
        player.addScoringToken(token_1);

        assertEquals(player.cannotTake(), List.of(0, 1));
    }

    /**
     * Test {@code Player}'s method {@code firstToFinish}, when the {@code Player} doesn't have an ending token.
     */
    @Test
    public void testFirstToFinishWithoutEndingToken(){
        Player player = null;
        try {
            player = new Player();
        } catch (IOException e) {
            fail();
        }

        assertFalse(player.firstToFinish());
    }
}
