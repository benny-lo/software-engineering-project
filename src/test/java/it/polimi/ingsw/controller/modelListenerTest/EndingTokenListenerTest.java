package it.polimi.ingsw.controller.modelListenerTest;
import it.polimi.ingsw.controller.modelListener.EndingTokenListener;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class EndingTokenListenerTest {
    /**
     * Testing for the constructor and getter for the class
     */
    @Test
    public void testConstructorGetter()
    {
        EndingTokenListener endingTokenListener = new EndingTokenListener();
        assertFalse(endingTokenListener.hasChanged());
        assertNull(endingTokenListener.getEndingToken());
    }

    /**
     * Testing for the updateState method working as intended
     */
    @Test
    public void testUpdateState()
    {
        EndingTokenListener endingTokenListener = new EndingTokenListener();
        assertFalse(endingTokenListener.hasChanged());
        assertNull(endingTokenListener.getEndingToken());
        endingTokenListener.updateState("nick");
        assertTrue(endingTokenListener.hasChanged());
        assertEquals("nick", endingTokenListener.getEndingToken());
        assertNull(endingTokenListener.getEndingToken());
    }

}
