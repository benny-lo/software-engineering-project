package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;

import it.polimi.ingsw.utils.message.Message;
import it.polimi.ingsw.utils.message.server.*;

import it.polimi.ingsw.view.server.VirtualView;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Controller.
 */

public class ControllerTest {
    /**
     * Test {@code Controller}'s constructor.
     */
    @Test
    public void testControllerConstructor(){
        Logger.setTestMode(true);

        Controller controller = new Controller(4, 2, 0);

        assertEquals(4, controller.getNumberPlayers());
        assertEquals(2, controller.getNumberCommonGoalCards());
        assertFalse(controller.isStarted());
        assertNotNull(controller);
    }
    /**
     * Test {@code join} method with only 2 players, that log in successfully.
     */
    @Test
    public void testJoinSuccessfully(){
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        GameData gameData0 = (GameData) mock0.queue.remove();
        assertEquals(gameData0.getNumberPlayers(), 2);

        GameData gameData1 = (GameData) mock1.queue.remove();
        assertEquals(gameData1.getNumberPlayers(), 2);

        WaitingUpdate waitingUpdate0 = (WaitingUpdate) mock0.queue.remove();
        assertEquals(waitingUpdate0.getNickname(), view0.getNickname());

        WaitingUpdate waitingUpdate1 = (WaitingUpdate) mock1.queue.remove();
        assertEquals(waitingUpdate1.getNickname(), view1.getNickname());

    }

    /**
     * Test {@code login} method trying to join while the game has already started.
     */
    @Test
    public void testJoinGameAlreadyStarted() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        MockServerConnection mock2 = new MockServerConnection();
        VirtualView view2 = new VirtualView(mock2);
        view2.setNickname("tick");
        controller.join(view2);

        assertTrue(controller.isStarted());

        GameData gameData0 = (GameData) mock0.queue.remove();
        assertEquals(gameData0.getNumberPlayers(), 2);

        GameData gameData1 = (GameData) mock1.queue.remove();
        assertEquals(gameData1.getNumberPlayers(), 2);

        GameData gameData2 = (GameData) mock2.queue.remove();
        assertEquals(gameData2.getNumberPlayers(), -1);
    }

    /**
     * Test {@code join} method trying to join but the game has ended.
     */
    @Test
    public void testJoinGameAlreadyEnded() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setEnded();

        MockServerConnection mock2 = new MockServerConnection();
        VirtualView view2 = new VirtualView(mock2);
        view2.setNickname("tick");
        controller.join(view2);

        GameData gameData0 = (GameData) mock0.queue.remove();
        assertEquals(gameData0.getNumberPlayers(), 2);

        GameData gameData1 = (GameData) mock1.queue.remove();
        assertEquals(gameData1.getNumberPlayers(), 2);

        GameData gameData2 = (GameData) mock2.queue.remove();
        assertEquals(gameData2.getNumberPlayers(), -1);
    }

    /**
     * Test {@code livingRoom} method but the game is ended.
     */
    @Test
    public void testLivingRoomGameAlreadyEnded() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.setEnded();
        controller.livingRoom(new ArrayList<>(List.of(new Position(1, 3))), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof SelectedItems selectedItems)
                assertNull(selectedItems.getItems());
        }
    }

    /**
     * Test {@code livingRoom} method when the sender is not the current player.
     */
    @Test
    public void testLivingRoomWrongCurrentPlayer() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1, 3))), view1);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof SelectedItems selectedItems)
                assertNull(selectedItems.getItems());
        }
    }

    /**
     * Test {@code livingRoom} method when it's not the right game phase.
     */
    @Test
    public void testLivingRoomWrongGamePhase() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1, 3))), view0);

        while(!mock0.queue.isEmpty()) {
            mock0.queue.remove();
        }

        controller.livingRoom(new ArrayList<>(List.of(new Position(1, 3))), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof SelectedItems selectedItems)
                assertNull(selectedItems.getItems());
        }
    }

    /**
     * Test {@code livingRoom} method but the item tiles aren't selectable.
     */
    @Test
    public void testLivingRoomWrongTiles() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(2,4))), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof SelectedItems selectedItems)
                assertNull(selectedItems.getItems());
        }
    }

    /**
     * Test {@code livingRoom} method picking a selectable tile.
     */
    @Test
    public void testLivingRoomSuccessfully() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        Message message;
        
        Map<Position, Item> oldLivingRoom = new HashMap<>();
        //saving the old livingRoom
        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof LivingRoomUpdate livingRoomUpdate)
                oldLivingRoom = livingRoomUpdate.getLivingRoomUpdate();
        }

        Position selectedPosition = new Position(1, 3);
        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(selectedPosition)), view0);

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();

            if (message instanceof SelectedItems selectedItems) {
                assertNotEquals(selectedItems.getItems().get(0), Item.LOCKED);
                assertNotNull(selectedItems.getItems());
            }

            else if (message instanceof LivingRoomUpdate livingRoomUpdate) {
                assertNull(livingRoomUpdate.getLivingRoomUpdate().get(selectedPosition));
                for (Position position : livingRoomUpdate.getLivingRoomUpdate().keySet()) {
                    if (!position.equals(selectedPosition))
                        assertEquals(livingRoomUpdate.getLivingRoomUpdate().get(position), oldLivingRoom.get(position));
                }
            }
        }
    }

    /**
     * Test {@code bookshelf} method but the game has already ended.
     */
    @Test
    public void testBookshelfGameAlreadyEnded() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1,3))), view0);

        controller.setEnded();
        controller.bookshelf(0, new ArrayList<>(List.of(0)), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method when the sender is not the current player.
     */
    @Test
    public void testBookshelfWrongCurrentPlayer() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1,3))), view0);

        controller.bookshelf(0, new ArrayList<>(List.of(0)), view1);

        Message message;

        while(!mock1.queue.isEmpty()) {
            message = mock1.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method when it's not the right game phase.
     */
    @Test
    public void testBookshelfWrongGamePhase() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.bookshelf(0, new ArrayList<>(List.of(0)), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method but the column is already full.
     */
    @Test
    public void testBookshelfFullColumn() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1,3), new Position(1, 4))), view0);
        controller.bookshelf(0, new ArrayList<>(List.of(0, 1)), view0);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(4,7), new Position(3, 7))), view0);
        controller.bookshelf(0, new ArrayList<>(List.of(1, 0)), view0);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(4,1), new Position(5, 1))), view0);
        controller.bookshelf(0, new ArrayList<>(List.of(1, 0)), view0);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(2,3))), view0);

        while(!mock0.queue.isEmpty()) {
            mock0.queue.remove();
        }

        controller.bookshelf(0, new ArrayList<>(List.of(0)), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method but the column is wrong.
     */
    @Test
    public void testBookshelfWrongColumn() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(1,3))), view0);
        controller.bookshelf(6, new ArrayList<>(List.of(0)), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method but the order is wrong.
     */
    @Test
    public void testBookshelfWrongOrder() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setCurrentPlayer(view0.getNickname());
        controller.livingRoom(new ArrayList<>(List.of(new Position(3,2))), view0);
        controller.bookshelf(0, new ArrayList<>(List.of(0, 1)), view0);

        Message message;

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertFalse(acceptedInsertion.isAccepted());
        }
    }

    /**
     * Test {@code bookshelf} method choosing a right column and order.
     */
    @Test
    public void testBookshelfSuccessfully() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2, 2, 0);

        MockServerConnection mock0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mock0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mock1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mock1);
        view1.setNickname("rick");
        controller.join(view1);

        Message message;
        
        int id = 0;
        Map<Position, Item> oldLivingRoom = new HashMap<>();
        //saving view0's id and the livingRoom
        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();
            if (message instanceof PersonalGoalCardUpdate personalGoalCardUpdate)
                id= personalGoalCardUpdate.getId();

            else if (message instanceof LivingRoomUpdate livingRoomUpdate)
                oldLivingRoom = livingRoomUpdate.getLivingRoomUpdate();
        }

        controller.setCurrentPlayer(view0.getNickname());

        Position selectedPosition = new Position(1, 3);
        controller.livingRoom(new ArrayList<>(List.of(selectedPosition)), view0);
        
        Map<Position, Item> newLivingRoom = new HashMap<>();
        Map<Position, Item> bookshelf0 = new HashMap<>();
        Map<Position, Item> bookshelf1 = new HashMap<>();

        //saving the updated livingRoom and the updated bookshelves
        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();

            if (message instanceof LivingRoomUpdate livingRoomUpdate)
                newLivingRoom = livingRoomUpdate.getLivingRoomUpdate();

            else if (message instanceof BookshelfUpdate bookshelfUpdate) {
                if (bookshelfUpdate.getOwner().equals(view0.getNickname()))
                    bookshelf0 = bookshelfUpdate.getBookshelf();
                else if (bookshelfUpdate.getOwner().equals(view1.getNickname()))
                    bookshelf1 = bookshelfUpdate.getBookshelf();
            }
        }


        controller.bookshelf(0, new ArrayList<>(List.of(0)), view0);

        while(!mock0.queue.isEmpty()) {
            message = mock0.queue.remove();

            if (message instanceof AcceptedInsertion acceptedInsertion)
                assertTrue(acceptedInsertion.isAccepted());

            else if (message instanceof LivingRoomUpdate livingRoomUpdate) {
                for (Position position : livingRoomUpdate.getLivingRoomUpdate().keySet()) {
                    assertEquals(newLivingRoom.get(position), (livingRoomUpdate.getLivingRoomUpdate().get(position)));
                }
            }

            else if (message instanceof BookshelfUpdate bookshelfUpdate) {
                assertTrue(bookshelfUpdate.getOwner().equals(view0.getNickname()) || bookshelfUpdate.getOwner().equals(view1.getNickname()));
                if (bookshelfUpdate.getOwner().equals(view0.getNickname())) {
                    assertNotNull(bookshelfUpdate.getBookshelf().get(new Position(0, 0)));
                    assertEquals(bookshelfUpdate.getBookshelf().get(new Position(0, 0)), oldLivingRoom.get(selectedPosition));
                    for (Position position : bookshelfUpdate.getBookshelf().keySet()) {
                        if (!position.equals(new Position(0, 0)))
                            assertEquals(bookshelfUpdate.getBookshelf().get(position), bookshelf0.get(position));
                    }
                }
                else if (bookshelfUpdate.getOwner().equals(view1.getNickname())) {
                    for (Position position : bookshelfUpdate.getBookshelf().keySet()) {
                        assertEquals(bookshelf1.get(position), bookshelfUpdate.getBookshelf().get(position));
                    }
                }
            }
            
            else if (message instanceof PersonalGoalCardUpdate personalGoalCardUpdate) {
                assertEquals(id, personalGoalCardUpdate.getId());
            }

            else if (message instanceof CommonGoalCardsUpdate commonGoalCardsUpdate) {
                assertNotNull(commonGoalCardsUpdate.getCommonGoalCardsUpdate());
                assertEquals(0, commonGoalCardsUpdate.getCommonGoalCardsUpdate().size());
            }

            else if (message instanceof EndingTokenUpdate endingTokenUpdate) {
                assertNull(endingTokenUpdate.getOwner());
            }

            else if (message instanceof ScoresUpdate scoresUpdate) {
                assertEquals(2, scoresUpdate.getScores().size());
            }

            else if (message instanceof StartTurnUpdate startTurnUpdate) {
                assertEquals(view1.getNickname(), startTurnUpdate.getCurrentPlayer());
            }
        }
    }

    /**
     * Test {@code chat} method but the game has already ended.
     */
    @Test
    public void testChatGameAlreadyEnded() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.setEnded();

        controller.chat("Good Game", "all", view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();
            if (message instanceof ChatAccepted chatAccepted)
                assertFalse(chatAccepted.isAccepted());
        }
    }

    /**
     * Test {@code chat} method successfully sending a message to 'all'.
     */
    @Test
    public void testChatToAllSuccessfully() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.chat("Hello", "all", view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof ChatAccepted chatAccepted)
                assertTrue(chatAccepted.isAccepted());

            else if (message instanceof ChatUpdate chatUpdate) {
                assertEquals("all", chatUpdate.getReceiver());
                assertEquals("Hello", chatUpdate.getText());
                assertEquals("nick", chatUpdate.getSender());
            }
        }

        while(!mockServerConnection1.queue.isEmpty()) {
            message = mockServerConnection1.queue.remove();

            if (message instanceof ChatUpdate chatUpdate) {
                assertEquals("all", chatUpdate.getReceiver());
                assertEquals("Hello", chatUpdate.getText());
                assertEquals("nick", chatUpdate.getSender());
            }
        }
    }

    /**
     * Test {@code chat} method when successfully sends a message to another specific player.
     */
    @Test
    public void testChatToPlayerSuccessfully() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.chat("Hello", "rick", view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof ChatAccepted chatAccepted)
                assertTrue(chatAccepted.isAccepted());

            else if (message instanceof ChatUpdate chatUpdate) {
                assertEquals("nick", chatUpdate.getSender());
                assertEquals("rick", chatUpdate.getReceiver());
                assertEquals("Hello", chatUpdate.getText());
            }
        }
    }

    /**
     * Test {@code chat} method when sends a message a player that not exists.
     */
    @Test
    public void testChatToNotExistentPlayer() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.chat("Hello", "john", view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof ChatAccepted chatAccepted)
                assertFalse(chatAccepted.isAccepted());
        }
    }

    /**
     * Test {@code chat} method when a player sends a message to himself.
     */
    @Test
    public void testChatWithReceiverEqualsToSender() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.chat("Hello", "nick", view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof ChatAccepted chatAccepted)
                assertFalse(chatAccepted.isAccepted());
        }
    }

    /**
     * Test {@code disconnection} after the game is started.
     */
    @Test
    public void testDisconnectionSuccessfully() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        MockServerConnection mockServerConnection1 = new MockServerConnection();
        VirtualView view1 = new VirtualView(mockServerConnection1);
        view1.setNickname("rick");
        controller.join(view1);

        controller.disconnection(view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof EndGameUpdate endGameUpdate)
                assertNull(endGameUpdate.getWinner());
        }
    }

    /**
     * Test {@code disconnection} before the game has started.
     */
    @Test
    public void testDisconnectionInWaitingRoom() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        while(!mockServerConnection0.queue.isEmpty()) {
            mockServerConnection0.queue.remove();
        }

        controller.disconnection(view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof WaitingUpdate waitingUpdate)
                assertFalse(waitingUpdate.isConnected());
        }
    }

    /**
     * Test {@code disconnection} when the game in already ended.
     */
    @Test
    public void testDisconnectionGameAlreadyEnded() {
        Logger.setTestMode(true);

        Controller controller = new Controller(2,2, 0);

        MockServerConnection mockServerConnection0 = new MockServerConnection();
        VirtualView view0 = new VirtualView(mockServerConnection0);
        view0.setNickname("nick");
        controller.join(view0);

        while(!mockServerConnection0.queue.isEmpty()) {
            mockServerConnection0.queue.remove();
        }

        controller.setEnded();

        controller.disconnection(view0);

        Message message;

        while(!mockServerConnection0.queue.isEmpty()) {
            message = mockServerConnection0.queue.remove();

            if (message instanceof WaitingUpdate waitingUpdate)
                assertFalse(waitingUpdate.isConnected());
        }
    }
}
