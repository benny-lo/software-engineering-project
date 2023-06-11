package it.polimi.ingsw.controller;


import it.polimi.ingsw.utils.classesOnlyForTesting.MockServerConnection;

import it.polimi.ingsw.utils.message.server.GamesList;
import it.polimi.ingsw.view.server.VirtualView;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Lobby
 */

public class LobbyTest {

    /**
     * Testing the login method when it fails because the nickname is already chosen.
     */
    @Test
    public void testLoginFailedChosen()
    {
        Lobby lobby = Lobby.getInstance();
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.login(view0.getNickname(), view0);
        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("nick");
        lobby.login(view1.getNickname(),view1);
        GamesList gamesList = (GamesList) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertNull(gamesList.getAvailable());
    }

    /**
     * Testing the Login method when it fails because the view is already logged in
     */
    @Test
    public void testLoginFailedLogged()
    {
        Lobby lobby = Lobby.getInstance();
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.login(view0.getNickname(), view0);
        view0.setNickname("rick");
        lobby.login(view0.getNickname(), view0);
        GamesList gamesList = (GamesList) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertNull(gamesList.getAvailable());
    }


}
