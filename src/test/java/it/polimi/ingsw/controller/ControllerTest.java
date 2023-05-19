package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.forTesting.MockVirtualView;
import it.polimi.ingsw.network.VirtualView;
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
     * Test {@code update(JoinAction action)}'s method with only 2 players.
     */
    @Test
    public void testSuccessfulJoinAction(){
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        assertTrue(controller.getAllPlayers().contains(view0.getNickname()));
        assertTrue(controller.getAllPlayers().contains(view1.getNickname()));
    }

    /**
     * Test {@code update(JoinAction action)}'s method trying to join while the game is already started.
     */
    @Test
    public void testUnsuccessfulJoinAction1() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        // ClientTesting client2 = new ClientTesting();
        // ServerSender Sender2 = new SenderClientTesting(client2);
        VirtualView view2 = new MockVirtualView(lobby);
        // view2.setToClient(Sender2);
        controller.update(new JoinAction(view2.getNickname(), view2));

        assertTrue(controller.isStarted());

        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        // assertTrue(view2.isError());
    }

    /**
     * Test {@code update(JoinAction action)}'s method trying to join but the game is ended.
     */
    @Test
    public void testUnsuccessfulJoinAction2() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setEnded();

        // ClientTesting client2 = new ClientTesting();
        // ServerSender Sender2 = new SenderClientTesting(client2);
        VirtualView view2 = new MockVirtualView(lobby);
        // view2.setToClient(Sender2);
        controller.update(new JoinAction(view2.getNickname(), view2));


        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        // assertTrue(view2.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction1() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.setEnded();
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>()));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction2() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view1.getNickname(), new LinkedList<>()));

        // assertTrue(view1.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction3() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>()));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method but the item tiles aren't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction4() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(0,0)))));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method picking a selectable tile.
     */
    @Test
    public void testSuccessfulSelectionFromLivingRoomAction() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3)))));

        // assertFalse(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder1 action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder1() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.setEnded();
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>()));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder2() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view1.getNickname(),0, new LinkedList<>()));

        // assertTrue(view1.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder3() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(),0, new LinkedList<>()));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method but the column isn't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder4() {
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        // view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0, 1))));

        // assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(4,7), new Position(3, 7)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(1, 0))));

        // assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(4,1), new Position(5, 1)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(1, 0))));

        // assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(3,2)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0))));

        // assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method choosing a right column.
     */
    @Test
    public void testSuccessfulSelectionColumnAndOrder(){
        Lobby lobby = new Lobby();
        Controller controller = new Controller(2, 2);
        // ClientTesting client0 = new ClientTesting();
        // ServerSender Sender0 = new SenderClientTesting(client0);
        VirtualView view0 = new MockVirtualView(lobby);
        //view0.setToClient(Sender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        // ClientTesting client1 = new ClientTesting();
        // ServerSender Sender1 = new SenderClientTesting(client1);
        VirtualView view1 = new MockVirtualView(lobby);
        // view1.setToClient(Sender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0, 1))));

        // assertFalse(view0.isError());
    }
}

