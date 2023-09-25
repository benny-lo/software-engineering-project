package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.client.GameInitialization;
import it.polimi.ingsw.utils.message.client.GameSelection;
import it.polimi.ingsw.utils.message.client.Nickname;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.VirtualView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Lobby.
 */

public class LobbyTest {

    /**
     * Testing the {@code login} method when it fails because the nickname is already chosen.
     */
    @Test
    public void testFailedLoginAlreadyChosenNickname() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.login(new Nickname("nick"));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.login(new Nickname("nick"));

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.poll();
            assertTrue(message instanceof GamesList);
            GamesList gamesList = (GamesList) message;
            assertNotNull(gamesList.getAvailable());
            assertTrue(gamesList.getAvailable().isEmpty());
        }

        while(!mockServerConnection1.queue.isEmpty()) {
            message = mockServerConnection1.queue.poll();
            assertTrue(message instanceof GamesList);
            GamesList gamesList = (GamesList) message;
            assertNull(gamesList.getAvailable());
        }
    }

    /**
     * Testing the {@code login} method when it fails because the view is already logged in.
     */
    @Test
    public void testFailedLoginAlreadyLoggedIn() {
        Logger.setTestMode(true);

        Lobby.setNull();

        Message message;

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.login(new Nickname("nick"));

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.poll();
            assertTrue(message instanceof GamesList);
            GamesList gamesList = (GamesList) message;
            assertNotNull(gamesList.getAvailable());
            assertTrue(gamesList.getAvailable().isEmpty());
        }

        view0.login(new Nickname("rick"));


        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GamesList gamesList) {
                assertNotNull(gamesList.getAvailable());
                assertTrue(gamesList.getAvailable().isEmpty());
            }
        }
    }

    /**
     * Testing the {@code createGame} method when the view is not logged in.
     */
    @Test
    public void testCreateGameNotLoggedIn() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.createGame(new GameInitialization(2, 2));

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GameData gameData) {
                assertEquals(gameData.getNumberPlayers(),-1);
                assertNull(gameData.getConnectedPlayers());
                assertEquals(gameData.getNumberCommonGoalCards(),-1);
                assertEquals(gameData.getLivingRoomRows(),-1);
                assertEquals(gameData.getLivingRoomColumns(),-1);
                assertEquals(gameData.getBookshelvesRows(),-1);
                assertEquals(gameData.getBookshelvesColumns(),-1);
            }
        }
    }

    /**
     * Testing the {@code createGame} method when it is given incorrect parameters.
     */
    @Test
    public void testCreateGameIncorrectParameters() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.login(new Nickname("rick"));
        view0.createGame(new GameInitialization(1, 2));


        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GamesList gamesList) {
                assertNotNull(gamesList.getAvailable());
                assertTrue(gamesList.getAvailable().isEmpty());
            }
            if (message instanceof GameData gameData) {
                assertEquals(gameData.getNumberPlayers(),-1);
                assertNull(gameData.getConnectedPlayers());
                assertEquals(gameData.getNumberCommonGoalCards(),-1);
                assertEquals(gameData.getLivingRoomRows(),-1);
                assertEquals(gameData.getLivingRoomColumns(),-1);
                assertEquals(gameData.getBookshelvesRows(),-1);
                assertEquals(gameData.getBookshelvesColumns(),-1);
            }
        }
    }

    /**
     * Testing the {@code createGame} method with correct parameters.
     */
    @Test
    public void testCreateGameLoggedIn() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.login(new Nickname("nick"));
        view0.createGame(new GameInitialization(2, 2));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);

        view1.login(new Nickname("rick"));

        Message message;

        while(!mockServerConnection1.queue.isEmpty()) {
            message = mockServerConnection1.queue.remove();
            if (message instanceof GamesList gamesList) {
                assertEquals(1, gamesList.getAvailable().size());
                assertEquals(0, gamesList.getAvailable().get(0).getId());
                assertEquals(2, gamesList.getAvailable().get(0).getNumberPlayers());
                assertEquals(2, gamesList.getAvailable().get(0).getNumberCommonGoals());
            }
        }
    }

    /**
     * Testing the {@code selectGame} method when the player is not logged in.
     */
    @Test
    public void testSelectGameNotLoggedIn() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.login(new Nickname("nick"));

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GameData gameData) {
                assertEquals(gameData.getNumberPlayers(),-1);
                assertNull(gameData.getConnectedPlayers());
                assertEquals(gameData.getNumberCommonGoalCards(),-1);
                assertEquals(gameData.getLivingRoomRows(),-1);
                assertEquals(gameData.getLivingRoomColumns(),-1);
                assertEquals(gameData.getBookshelvesRows(),-1);
                assertEquals(gameData.getBookshelvesColumns(),-1);
            }
        }
    }

    /**
     * Testing the {@code selectGame} method when the player selects a game that does not exist.
     */
    @Test
    public void testSelectGameWrongSelection() {
        Logger.setTestMode(true);

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.login(new Nickname("nick"));
        view0.selectGame(new GameSelection(0));

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GameData gameData) {
                assertEquals(gameData.getNumberPlayers(), -1);
                assertNull(gameData.getConnectedPlayers());
                assertEquals(gameData.getNumberCommonGoalCards(), -1);
                assertEquals(gameData.getLivingRoomRows(), -1);
                assertEquals(gameData.getLivingRoomColumns(), -1);
                assertEquals(gameData.getBookshelvesRows(), -1);
                assertEquals(gameData.getBookshelvesColumns(), -1);
            }
        }
    }

    /**
     * Testing the {@code selectGame} method when it works as intended, checking every attribute.
     */
    @Test
    public void testSelectGame() {
        Logger.setTestMode(true);

        int gameList=0;
        int gameData=0;
        int waiting=0;
        int bookshelf=0;
        int commonGoalCards=0;
        int endingToken=0;
        int livingRoom=0;
        int personalGoalCards=0;
        int scores=0;
        int startTurn=0;

        Lobby.setNull();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        view0.login(new Nickname("nick"));
        view0.createGame(new GameInitialization(2, 2));

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);

        view1.login(new Nickname("rick"));
        view1.selectGame(new GameSelection(0));

        for(Message message :mockServerConnection0.queue) {
            if(message instanceof GamesList) {
                gameList++;
            }
            if(message instanceof GameData) {
                gameData++;
            }
            if(message instanceof WaitingUpdate) {
                waiting++;
            }
            if(message instanceof BookshelfUpdate) {
                bookshelf++;
            }
            if(message instanceof CommonGoalCardsUpdate) {
                commonGoalCards++;
            }
            if(message instanceof EndingTokenUpdate) {
                endingToken++;
            }
            if(message instanceof LivingRoomUpdate) {
                livingRoom++;
            }
            if(message instanceof PersonalGoalCardUpdate) {
                personalGoalCards++;
            }
            if(message instanceof ScoresUpdate) {
                scores++;
            }
            if(message instanceof StartTurnUpdate) {
                startTurn++;
            }
        }

        for(Message message :mockServerConnection1.queue) {
            if(message instanceof GamesList) {
                gameList++;
            }
            if(message instanceof GameData) {
                gameData++;
            }
            if(message instanceof WaitingUpdate) {
                waiting++;
            }
            if(message instanceof BookshelfUpdate) {
                bookshelf++;
            }
            if(message instanceof CommonGoalCardsUpdate) {
                commonGoalCards++;
            }
            if(message instanceof EndingTokenUpdate) {
                endingToken++;
            }
            if(message instanceof LivingRoomUpdate) {
                livingRoom++;
            }
            if(message instanceof PersonalGoalCardUpdate) {
                personalGoalCards++;
            }
            if(message instanceof ScoresUpdate) {
                scores++;
            }
            if(message instanceof StartTurnUpdate) {
                startTurn++;
            }
        }

        assertEquals(2, gameList);
        assertEquals(2, gameData);
        assertEquals(3, waiting);
        assertEquals(4, bookshelf);
        assertEquals(2, commonGoalCards);
        assertEquals(2, endingToken);
        assertEquals(2, livingRoom);
        assertEquals(2, personalGoalCards);
        assertEquals(2, scores);
        assertEquals(2, startTurn);
    }

    /**
     * Test that a {@code Controller} is removed from {@code Lobby} when all views connected to it disconnect. Moreover,
     * it is tested that all other views receive the correct updates, i.e. the game has been deleted.
     */
    @Test
    public void testRemoveControllerAndView() {
        Logger.setTestMode(true);
        Lobby.setNull();

        MockServerConnection alice = new MockServerConnection();
        MockServerConnection bob = new MockServerConnection();

        VirtualView aliceView = new VirtualView(alice);
        VirtualView bobView = new VirtualView(bob);

        aliceView.login(new Nickname("alice"));
        Message aliceMessage = alice.queue.poll();
        assertTrue(aliceMessage instanceof GamesList);
        assertNotNull(((GamesList) aliceMessage).getAvailable());
        assertTrue(((GamesList) aliceMessage).getAvailable().isEmpty());

        aliceView.createGame(new GameInitialization(2, 2));
        aliceMessage = alice.queue.poll();
        assertTrue(aliceMessage instanceof GameData);
        assertEquals(2, ((GameData) aliceMessage).getNumberPlayers());

        bobView.login(new Nickname("bob"));
        Message bobMessage = bob.queue.poll();
        assertTrue(bobMessage instanceof GamesList);
        assertNotNull(((GamesList) bobMessage).getAvailable());
        assertEquals(1, ((GamesList) bobMessage).getAvailable().size());
    }

    /**
     * Test the creation of a game with two players (alice and bob). Then the disconnection of Bob followed by his
     * reconnection.
     */
    @Test
    public void testReconnection() {
        Logger.setTestMode(true);
        Lobby.setNull();

        MockServerConnection alice = new MockServerConnection();
        MockServerConnection bob = new MockServerConnection();

        VirtualView aliceView = new VirtualView(alice);
        VirtualView bobView = new VirtualView(bob);

        aliceView.login(new Nickname("alice"));
        bobView.login(new Nickname("bob"));

        aliceView.createGame(new GameInitialization(2, 2));
        bobView.selectGame(new GameSelection(0));

        bobView.disconnect();

        alice.queue.clear();

        MockServerConnection bobReconnected = new MockServerConnection();
        VirtualView bobReconnectedView = new VirtualView(bobReconnected);

        bobReconnectedView.login(new Nickname("bob"));

        Message bobMessage = bobReconnected.queue.poll();
        assertTrue(bobMessage instanceof Reconnection);
        assertEquals("bob", ((Reconnection) bobMessage).getReconnectedPlayer());

        Message aliceMessage = alice.queue.poll();
        assertTrue(aliceMessage instanceof Reconnection);
        assertEquals("bob", ((Reconnection) aliceMessage).getReconnectedPlayer());
    }
}
