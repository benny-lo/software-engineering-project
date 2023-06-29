package it.polimi.ingsw.model.commonGoalCardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commonGoalCard.CommonGoalCardManager;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Unit tests for CommonGoalCardManager
 */
public class CommonGoalCardManagerTest {
    /**
     * Test {@code CommonGoalCardManager}'s constructor, it is not null.
     */
    @Test
    public void testCommonGoalCardManagerConstructor(){
        CommonGoalCardManager commonGoalCardManager = new CommonGoalCardManager(2, 2);

        assertNotNull(commonGoalCardManager);
    }

    /**
     * Test {@code CommonGoalManager}'s method {@code check}, there are 2 matches.
     */
    @Test
    public void testCheck2Matches(){
        CommonGoalCardManager commonGoalCardManager = new CommonGoalCardManager(2);
        Bookshelf bookshelf = new Bookshelf(3,3);
        List<Integer> cannotTake = new ArrayList<>();
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.CAT, Item.CAT)),0);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.PLANT, Item.CAT)),2);

        List<ScoringToken> scoringToken = commonGoalCardManager.check(bookshelf, cannotTake);
        assertEquals(8, scoringToken.get(0).getScore());
        assertEquals(7, scoringToken.get(0).getType());
        cannotTake = List.of(scoringToken.get(0).getType());

        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.CAT, Item.CAT)),1);

        scoringToken = commonGoalCardManager.check(bookshelf, cannotTake);
        assertEquals(8, scoringToken.get(0).getScore());
        assertEquals(8, scoringToken.get(0).getType());
    }

    /**
     * Test for method {@code check} of {@code CommonGoalCardManager}, only one of the two common goals is
     * satisfied and the corresponding token cannot be taken as the common goal was already satisfied.
     */
    @Test
    public void testCheck1MatchButAlreadyTaken() {
        CommonGoalCardManager commonGoalCardManager = new CommonGoalCardManager(2);
        Bookshelf bookshelf = new Bookshelf(2, 2);

        List<Item> toInsert = List.of(Item.CAT, Item.CAT);
        bookshelf.insert(toInsert, 0);
        bookshelf.insert(toInsert, 1);

        List<ScoringToken> ret = commonGoalCardManager.check(bookshelf, List.of(7));
        assertTrue(ret.isEmpty());
    }

    /**
     * Test {@code CommonGoalManager}'s method {@code check}, there isn't a match.
     */
    @Test
    public void testCheckWithoutMatches(){
        CommonGoalCardManager commonGoalCardManager = new CommonGoalCardManager(2);
        Bookshelf bookshelf = new Bookshelf();
        List<Integer> cannotTake = new ArrayList<>();
        bookshelf.insert(new LinkedList<>(List.of(Item.PLANT, Item.CAT, Item.CAT)),0);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.PLANT, Item.CAT)),2);

        assertEquals(new ArrayList<>(), commonGoalCardManager.check(bookshelf, cannotTake));
    }
}
