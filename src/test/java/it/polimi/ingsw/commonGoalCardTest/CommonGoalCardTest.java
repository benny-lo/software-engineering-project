package it.polimi.ingsw.commonGoalCardTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commonGoalCard.CommonGoalCard;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPattern8;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * Unit tests for CommonGoalCard.
 */
public class CommonGoalCardTest {
    /**
     * Test {@code CommonGoalCard}'s Constructor, it is not null.
     */
    @Test
    public void testCommonGoalCardConstructor() {
        CommonGoalPatternInterface commonGoalPatternInterface = new CommonGoalPattern8();
        CommonGoalCard commonGoalCard = new CommonGoalCard(1, 2, commonGoalPatternInterface);

        assertNotNull(commonGoalCard);
    }

    /**
     * Test {@code CommonGoalCard}'s method {@code popToken} with 4 players popping every {@code ScoringToken} out.
     */
    @Test
    public void testPopTokenWith4Players(){
        CommonGoalPatternInterface commonGoalPatternInterface = new CommonGoalPattern8();
        CommonGoalCard commonGoalCard = new CommonGoalCard(1, 4, commonGoalPatternInterface);

        ScoringToken scoringToken = commonGoalCard.popToken();
        assertEquals(8, scoringToken.getScore());
        assertEquals(1, scoringToken.getType());

        scoringToken = commonGoalCard.popToken();
        assertEquals(6, scoringToken.getScore());
        assertEquals(1, scoringToken.getType());

        scoringToken = commonGoalCard.popToken();
        assertEquals(4, scoringToken.getScore());
        assertEquals(1, scoringToken.getType());

        scoringToken = commonGoalCard.popToken();
        assertEquals(2, scoringToken.getScore());
        assertEquals(1, scoringToken.getType());

        assertThrows(EmptyStackException.class, commonGoalCard::popToken);
    }

    /**
     * Test {@code CommonGoalCard}'s method {@code checkPattern} with a match.
     */
    @Test
    public void testCheckPatternMatch(){
        CommonGoalPatternInterface commonGoalPatternInterface = new CommonGoalPattern8();
        CommonGoalCard commonGoalCard = new CommonGoalCard(1, 4, commonGoalPatternInterface);
        Bookshelf bookshelf = new Bookshelf(3, 3);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.BOOK, Item.CAT)),0);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.PLANT, Item.CAT)),2);

        assertTrue(commonGoalCard.checkPattern(bookshelf));
    }

    /**
     * Test {@code CommonGoalCard}'s method {@code checkPattern} without a corresponding bookshelf pattern.
     */
    @Test
    public void testCheckPatternNotAMatch(){
        CommonGoalPatternInterface commonGoalPatternInterface = new CommonGoalPattern8();
        CommonGoalCard commonGoalCard = new CommonGoalCard(1, 3, commonGoalPatternInterface);
        Bookshelf bookshelf = new Bookshelf(3, 3);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.BOOK, Item.CAT)),0);
        bookshelf.insert(new LinkedList<>(List.of(Item.CAT, Item.PLANT, Item.FRAME)),2);

        assertFalse(commonGoalCard.checkPattern(bookshelf));
    }
}
