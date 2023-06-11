package it.polimi.ingsw.controller;


import it.polimi.ingsw.utils.classesOnlyForTesting.MockServerConnection;

import it.polimi.ingsw.utils.message.server.GameData;
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
        Lobby.setNull();
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
        Lobby.setNull();
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

    /**
     * Testing the createGame method when the view is not logged in already or when it is given incorrect parameters
     */
    @Test
    public void testCreateGameNotLogged()
    {
        Lobby.setNull();
        Lobby lobby = Lobby.getInstance();
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        lobby.createGame(2,2,view0);
        GameData gameData0 = (GameData) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertEquals(gameData0.getNumberPlayers(),-1);
        assertNull(gameData0.getConnectedPlayers());
        assertEquals(gameData0.getNumberCommonGoalCards(),-1);
        assertEquals(gameData0.getLivingRoomRows(),-1);
        assertEquals(gameData0.getLivingRoomColumns(),-1);
        assertEquals(gameData0.getBookshelvesRows(),-1);
        assertEquals(gameData0.getBookshelvesColumns(),-1);
        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        lobby.login(view1.getNickname(), view1);
        lobby.createGame(1,2,view1);
        GameData gameData1 = (GameData) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertEquals(gameData1.getNumberPlayers(),-1);
        assertNull(gameData1.getConnectedPlayers());
        assertEquals(gameData1.getNumberCommonGoalCards(),-1);
        assertEquals(gameData1.getLivingRoomRows(),-1);
        assertEquals(gameData1.getLivingRoomColumns(),-1);
        assertEquals(gameData1.getBookshelvesRows(),-1);
        assertEquals(gameData1.getBookshelvesColumns(),-1);
    }

    /**
     * Testing the createGame method when it works as intended and the content of the messages when the login works as intended
     */
    @Test
    public void testCreateGame()
    {
        Lobby.setNull();
        Lobby lobby = Lobby.getInstance();
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.login(view0.getNickname(), view0);
        lobby.createGame(2,2,view0);
        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        lobby.login(view1.getNickname(), view1);
        GamesList gamesList = (GamesList) mockServerConnection1.list.get(mockServerConnection1.list.size()-1);
        assertEquals(gamesList.getAvailable().size(),1);
        assertEquals(gamesList.getAvailable().get(0).getId(),0);
        assertEquals(gamesList.getAvailable().get(0).getNumberPlayers(),2);
        assertEquals(gamesList.getAvailable().get(0).getNumberCommonGoals(),2);

    }

    /**
     * Testing the selectGame method when it fails.
     */
    @Test
    public void testSelectGameFail()
    {
        Lobby.setNull();
        Lobby lobby = Lobby.getInstance();
        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.selectGame(0,view0);
        GameData gameData0 = (GameData) mockServerConnection0.list.get(mockServerConnection0.list.size()-1);
        assertEquals(gameData0.getNumberPlayers(),-1);
        assertNull(gameData0.getConnectedPlayers());
        assertEquals(gameData0.getNumberCommonGoalCards(),-1);
        assertEquals(gameData0.getLivingRoomRows(),-1);
        assertEquals(gameData0.getLivingRoomColumns(),-1);
        assertEquals(gameData0.getBookshelvesRows(),-1);
        assertEquals(gameData0.getBookshelvesColumns(),-1);
        lobby.login(view0.getNickname(), view0);
    }



}
