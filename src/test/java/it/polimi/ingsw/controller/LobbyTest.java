package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.classesOnlyForTesting.MockServerConnection;
import it.polimi.ingsw.view.server.VirtualView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Lobby
 */

public class LobbyTest {

    /**
     * Test {@code Lobby}'s constructor
     */
    @Test
    public void testLobbyConstructor()
    {
        Lobby lobby = Lobby.getInstance();
        assertNotNull(lobby.getControllers());
        assertNotNull(lobby.getViews());
        assertEquals(lobby.getAvailableId(),0);

    }

    /**
     * Testing for adding and removing controllers and Virtual Views
     */
    @Test
    public void testAddingRemoving()
    {
        Lobby lobby = Lobby.getInstance();
        Controller controller = new Controller(2,2);
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        lobby.addController(controller);
        lobby.addVirtualView(view0);
        assertEquals(lobby.getControllers().get(0), controller);
        assertTrue(lobby.getViews().contains(view0));
        lobby.removeController(controller);
        lobby.removeVirtualView(view0);
    }
}
