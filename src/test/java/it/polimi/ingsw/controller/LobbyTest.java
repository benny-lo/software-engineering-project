package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.classesOnlyForTesting.MockServerConnection;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.GamesList;
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
        //assertEquals(lobby.getAvailableId(),0);


    }


    /**
     * Testing the login method when it fails because the nickname is already chosen.
     */
    @Test
    public void testLoginFailed()
    {
        int check = 0;
        Lobby lobby = Lobby.getInstance();
        Controller controller = new Controller(2,2);
        MockServerConnection mockServerConnection = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection);
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));
        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        lobby.login("nick", view1);
        GamesList gamesList = (GamesList) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertTrue(gamesList.getAvailable().isEmpty());
        for(Message message : mockServerConnection.list)
        {
            if(message instanceof GamesList)
            {
                check++;
            }
        }
        assertEquals(check,0);
    }
}
