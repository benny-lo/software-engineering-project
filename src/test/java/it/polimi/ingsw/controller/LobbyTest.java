package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.message.Message;
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
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.login(view0.getNickname(), view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("nick");
        lobby.login(view1.getNickname(),view1);

        Message message;

        while(!mockServerConnection1.queue.isEmpty()) {
            message = mockServerConnection1.queue.remove();
            if (message instanceof GamesList gamesList)
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
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        lobby.login(view0.getNickname(), view0);

        while(!mockServerConnection0.queue.isEmpty()) {
            mockServerConnection0.queue.remove();
        }

        view0.setNickname("rick");
        lobby.login(view0.getNickname(), view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof GamesList gamesList)
                assertNull(gamesList.getAvailable());
        }
    }

    /**
     * Testing the {@code createGame} method when the view is not logged in.
     */
    @Test
    public void testCreateGameNotLoggedIn() {
        Logger.setTestMode(true);

        Lobby.setNull();
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);

        lobby.createGame(2,2,view0);

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
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection0);
        view1.setNickname("rick");

        lobby.login(view1.getNickname(), view1);
        lobby.createGame(1,2,view1);


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
     * Testing the {@code createGame} method when it works as intended.
     */
    @Test
    public void testCreateGameLoggedIn() {
        Logger.setTestMode(true);

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

        Message message;

        while(!mockServerConnection1.queue.isEmpty()) {
            message = mockServerConnection1.queue.remove();
            if (message instanceof GamesList gamesList) {
                assertEquals(gamesList.getAvailable().size(),1);
                assertEquals(gamesList.getAvailable().get(0).getId(),0);
                assertEquals(gamesList.getAvailable().get(0).getNumberPlayers(),2);
                assertEquals(gamesList.getAvailable().get(0).getNumberCommonGoals(),2);
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
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");

        lobby.selectGame(0,view0);

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
     * Testing the {@code selectGame} method when the player selects a game that not exists.
     */
    @Test
    public void testSelectGameWrongSelection() {
        Logger.setTestMode(true);

        Lobby.setNull();
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");

        lobby.login(view0.getNickname(), view0);
        lobby.selectGame(0,view0);

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
        Lobby lobby = Lobby.getInstance();

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");

        lobby.login(view0.getNickname(),view0);
        lobby.createGame(2,2,view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");

        lobby.login(view1.getNickname(), view1);
        lobby.selectGame(0,view1);

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

        assertEquals(gameList,2);
        assertEquals(gameData,2);
        assertEquals(waiting,3);
        assertEquals(bookshelf,4);
        assertEquals(commonGoalCards,2);
        assertEquals(endingToken,2);
        assertEquals(livingRoom,2);
        assertEquals(personalGoalCards,2);
        assertEquals(scores,2);
        assertEquals(startTurn,2);
    }
}
