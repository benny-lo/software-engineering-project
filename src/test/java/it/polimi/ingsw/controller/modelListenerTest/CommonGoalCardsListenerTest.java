package it.polimi.ingsw.controller.modelListenerTest;
import it.polimi.ingsw.controller.modelListener.CommonGoalCardsListener;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class CommonGoalCardsListenerTest {
    /**
     * Testing for the class constructor
     */
    @Test
    public void constructorTest()
    {
        CommonGoalCardsListener commonGoalCardsListener = new CommonGoalCardsListener();
        assertFalse(commonGoalCardsListener.hasChanged());
        assertNotNull(commonGoalCardsListener);
        assertNotNull(commonGoalCardsListener.getCards());
        assertTrue(commonGoalCardsListener.getCards().isEmpty());

    }
    /**
     * Testing for the updateState method
     */
    @Test
    public void updateStateTest()
    {
        CommonGoalCardsListener commonGoalCardsListener = new CommonGoalCardsListener();
        assertFalse(commonGoalCardsListener.hasChanged());
        commonGoalCardsListener.updateState(1,1);
        commonGoalCardsListener.updateState(2,2);
        commonGoalCardsListener.updateState(3,3);
        assertTrue(commonGoalCardsListener.hasChanged());
        Map<Integer, Integer> map = commonGoalCardsListener.getCards();
        assertEquals(1,map.get(1));
        assertEquals(2,map.get(2));
        assertEquals(3,map.get(3));
        assertTrue(commonGoalCardsListener.getCards().isEmpty());
    }
}
