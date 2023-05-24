package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.classesOnlyForTesting.MockServerConnection;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;

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
        Controller controller = new Controller(4, 2);
        assertEquals(4, controller.getNumberPlayers());
        assertEquals(2, controller.getNumberCommonGoalCards());
        assertFalse(controller.isStarted());
    }

    /**
     * Test {@code perform(JoinAction action)}'s method with only 2 players.
     */
    @Test
    public void testSuccessfulJoinAction(){
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        assertTrue(controller.getAllPlayers().contains(view0.getNickname()));
        assertTrue(controller.getAllPlayers().contains(view1.getNickname()));

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));
    }

    /**
     * Test {@code perform(JoinAction action)}'s method trying to join while the game is already started.
     */
    @Test
    public void testUnsuccessfulJoinAction1() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        VirtualView view2 = new VirtualView(new MockServerConnection());
        view2.setNickname("tick");
        controller.perform(new JoinAction(view2));

        assertTrue(controller.isStarted());

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));

        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertFalse(controller.getViews().contains(view2));

        assertFalse(controller.getGameBuilder().getPlayers().contains(view2.getNickname()));

        assertFalse(controller.getBookshelfListenersOwners().contains(view2.getNickname()));

        assertFalse(controller.getGameBuilder().getBookshelfListenersOwners().contains(view2.getNickname()));
    }

    /**
     * Test {@code perform(JoinAction action)}'s method trying to join but the game is ended.
     */
    @Test
    public void testUnsuccessfulJoinAction2() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setEnded();

        VirtualView view2 = new VirtualView(new MockServerConnection());
        view2.setNickname("tick");
        controller.perform(new JoinAction(view2));

        assertTrue(controller.getViews().contains(view0));
        assertTrue(controller.getViews().contains(view1));

        assertTrue(controller.getGameBuilder().getPlayers().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getPlayers().contains(view1.getNickname()));

        assertTrue(controller.getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getBookshelfListenersOwners().contains(view1.getNickname()));

        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view0.getNickname()));
        assertTrue(controller.getGameBuilder().getBookshelfListenersOwners().contains(view1.getNickname()));

        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertFalse(controller.getViews().contains(view2));

        assertFalse(controller.getGameBuilder().getPlayers().contains(view2.getNickname()));

        assertFalse(controller.getBookshelfListenersOwners().contains(view2.getNickname()));

        assertFalse(controller.getGameBuilder().getBookshelfListenersOwners().contains(view2.getNickname()));
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction1() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.setEnded();
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction2() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view1, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction3() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method but the item tiles aren't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction4() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(0,0)))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionFromLivingRoomAction action)}'s method picking a selectable tile.
     */
    @Test
    public void testSuccessfulSelectionFromLivingRoomAction() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3)))));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder1 action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder1() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.setEnded();
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder2() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view1,0, new LinkedList<>()));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder3() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionColumnAndOrderAction(view0,0, new LinkedList<>()));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method but the column isn't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder4() {
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0, 1))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(4,7), new Position(3, 7)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(1, 0))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(4,1), new Position(5, 1)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(1, 0))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(3,2)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0))));

        assertEquals(TurnPhase.BOOKSHELF, controller.getTurnPhase());
    }

    /**
     * Test {@code perform(SelectionColumnAndOrder action)}'s method choosing a right column.
     */
    @Test
    public void testSuccessfulSelectionColumnAndOrder(){
        Controller controller = new Controller(2, 2);
        VirtualView view0 = new VirtualView(new MockServerConnection());
        view0.setNickname("nick");
        controller.perform(new JoinAction(view0));

        VirtualView view1 = new VirtualView(new MockServerConnection());
        view1.setNickname("rick");
        controller.perform(new JoinAction(view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.perform(new SelectionFromLivingRoomAction(view0, new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.perform(new SelectionColumnAndOrderAction(view0, 0, new ArrayList<>(List.of(0, 1))));

        assertEquals(TurnPhase.LIVING_ROOM, controller.getTurnPhase());
    }
}

