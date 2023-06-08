package it.polimi.ingsw.model.commonGoalCardTest.commonGoalPatternTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternInterface;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.CommonGoalPatternCountGroups;
import it.polimi.ingsw.model.player.Bookshelf;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the general patterns 1, 3, 4.
 */
public class CommonGoalCardPatternCountGroupsTest {
    /**
     * Test on 2x3 bookshelf with a horizontal z-like pattern.
     */
    @Test
    public void testHorizontalZBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,2, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf();

        bookshelf.insert(Item.CAT, 0); bookshelf.insert(Item.BOOK, 0);
        bookshelf.insert(Item.CAT, 1); bookshelf.insert(Item.CAT, 1);
        bookshelf.insert(Item.BOOK, 2); bookshelf.insert(Item.CAT, 2);

        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test on 3x2 bookshelf with a vertical z-like pattern.
     */
    @Test
    public void testVerticalZBookshelf() {
        CommonGoalPatternInterface pattern = new CommonGoalPatternCountGroups(2,2, (s) -> s.size() == 2);
        Bookshelf bookshelf = new Bookshelf();

        bookshelf.insert(Item.BOOK, 0); bookshelf.insert(Item.CAT, 0); bookshelf.insert(Item.CAT, 0);
        bookshelf.insert(Item.CAT, 1); bookshelf.insert(Item.CAT, 1); bookshelf.insert(Item.BOOK, 1);

        assertTrue(pattern.check(bookshelf));
    }

    /**
     * Test on 4x4 bookshelf with a *-like pattern.
     */
    @Test
    public void testStarLikePattern() {
        Bookshelf bookshelf = new Bookshelf();

        List<Item> cats = new ArrayList<>();
        for(int i = 0; i < 3; i++) cats.add(Item.CAT);

        bookshelf.insert(cats, 2);
        bookshelf.insert(Item.BOOK, 1); bookshelf.insert(cats, 1);
        bookshelf.insert(Item.BOOK, 0); bookshelf.insert(Item.CAT, 0);
        bookshelf.insert(Item.BOOK, 3); bookshelf.insert(Item.BOOK, 3); bookshelf.insert(Item.CAT, 3);

        assertTrue((new CommonGoalPatternCountGroups(4,2, (s) -> s.size() == 4)).check(bookshelf));
        assertFalse((new CommonGoalPatternCountGroups(4,3, (s) -> s.size() == 4)).check(bookshelf));
    }

    /**
     * Test on 8x2 bookshelf with a vertical comb-like pattern.
     */
    @Test
    public void testCombPattern() {
        Bookshelf bookshelf = new Bookshelf(8,2);
        List<Item> cats = new ArrayList<>(), alternate = new ArrayList<>();
        for(int i = 0; i < 8; i++) cats.add(Item.CAT);
        for(int i = 0; i < 4; i++) {
            alternate.add(Item.CAT);
            alternate.add(Item.BOOK);
        }

        bookshelf.insert(cats, 0);
        bookshelf.insert(alternate, 1);

        assertTrue((new CommonGoalPatternCountGroups(4,2, (s) -> s.size() == 4)).check(bookshelf));
        assertFalse((new CommonGoalPatternCountGroups(4,3, (s) -> s.size() == 4)).check(bookshelf));
    }

    /**
     * Test on 3x3 bookshelf with another type of *-like pattern.
     */
    @Test
    public void testAnotherStar() {
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.insert(Item.BOOK, 0); bookshelf.insert(Item.CAT, 0);
        bookshelf.insert(Item.CAT, 1); bookshelf.insert(Item.CAT, 1); bookshelf.insert(Item.CAT, 1);
        bookshelf.insert(Item.BOOK, 2); bookshelf.insert(Item.CAT, 2);

        assertTrue((new CommonGoalPatternCountGroups(2,1, (s) -> s.size() == 2)).check(bookshelf));
        assertTrue((new CommonGoalPatternCountGroups(4,1, (s) -> s.size() == 4)).check(bookshelf));
        assertFalse((new CommonGoalPatternCountGroups(2,2, (s) -> s.size() == 2)).check(bookshelf));
        assertFalse((new CommonGoalPatternCountGroups(4,2, (s) -> s.size() == 4)).check(bookshelf));
    }
}
