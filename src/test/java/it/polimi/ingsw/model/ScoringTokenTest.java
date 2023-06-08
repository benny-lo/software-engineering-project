package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.utils.game.ScoringToken;
import org.junit.jupiter.api.Test;

public class ScoringTokenTest {
    /**
     * Test for the class constructor
     */
    @Test
    public void constructorTest()
    {
        ScoringToken token = new ScoringToken(1,2);
        assertEquals(token.getScore(),1);
        assertEquals(token.getType(),2);

    }

}
