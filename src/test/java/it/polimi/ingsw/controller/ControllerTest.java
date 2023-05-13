package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.network.server.UpdateSender;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.forTesting.ClientTesting;
import it.polimi.ingsw.utils.forTesting.UpdateSenderClientTesting;
import it.polimi.ingsw.view.VirtualView;
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
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        assertTrue(controller.getAllPlayers().contains(view0.getNickname()));
        assertTrue(controller.getAllPlayers().contains(view1.getNickname()));
    }

    /**
     * Test {@code update(JoinAction action)}'s method trying to join while the game is already started.
     */
    @Test
    public void testUnsuccessfulJoinAction1(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        ClientTesting client2 = new ClientTesting();
        UpdateSender updateSender2 = new UpdateSenderClientTesting(client2);
        VirtualView view2 = new VirtualView("tick");
        view2.setToClient(updateSender2);
        controller.update(new JoinAction(view2.getNickname(), view2));

        assertTrue(controller.isStarted());

        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertTrue(view2.isError());
    }

    /**
     * Test {@code update(JoinAction action)}'s method trying to join but the game is ended.
     */
    @Test
    public void testUnsuccessfulJoinAction2(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setEnded();

        ClientTesting client2 = new ClientTesting();
        UpdateSender updateSender2 = new UpdateSenderClientTesting(client2);
        VirtualView view2 = new VirtualView("tick");
        view2.setToClient(updateSender2);
        controller.update(new JoinAction(view2.getNickname(), view2));


        for(String player : controller.getAllPlayers()) {
            assertNotEquals(player, view2.getNickname());
        }

        assertTrue(view2.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction1(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.setEnded();
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>()));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction2(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view1.getNickname(), new LinkedList<>()));

        assertTrue(view1.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction3(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>()));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method but the item tiles aren't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionFromLivingRoomAction4(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(0,0)))));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionFromLivingRoomAction action)}'s method picking a selectable tile.
     */
    @Test
    public void testSuccessfulSelectionFromLivingRoomAction(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3)))));

        assertFalse(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder1 action)}'s method but the game is ended.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder1(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.setEnded();
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>()));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method when the sender is not the current player.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder2(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view1.getNickname(),0, new LinkedList<>()));

        assertTrue(view1.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method when it's not the right game phase.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder3(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(),0, new LinkedList<>()));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method but the column isn't selectable.
     */
    @Test
    public void testUnsuccessfulSelectionColumnAndOrder4(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0, 1))));

        assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(4,7), new Position(3, 7)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(1, 0))));

        assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(4,1), new Position(5, 1)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(1, 0))));

        assertFalse(view0.isError());

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(3,2)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0))));

        assertTrue(view0.isError());
    }

    /**
     * Test {@code update(SelectionColumnAndOrder action)}'s method choosing a right column.
     */
    @Test
    public void testSuccessfulSelectionColumnAndOrder(){
        Controller controller = new Controller(2, 2);
        ClientTesting client0 = new ClientTesting();
        UpdateSender updateSender0 = new UpdateSenderClientTesting(client0);
        VirtualView view0 = new VirtualView("nick");
        view0.setToClient(updateSender0);
        controller.update(new JoinAction(view0.getNickname(), view0));

        ClientTesting client1 = new ClientTesting();
        UpdateSender updateSender1 = new UpdateSenderClientTesting(client1);
        VirtualView view1 = new VirtualView("rick");
        view1.setToClient(updateSender1);
        controller.update(new JoinAction(view1.getNickname(), view1));

        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.LIVING_ROOM);
        controller.update(new SelectionFromLivingRoomAction(view0.getNickname(), new LinkedList<>(List.of(new Position(1,3), new Position(2, 3)))));
        controller.setCurrentPlayer(view0.getNickname());
        controller.setTurnPhase(TurnPhase.BOOKSHELF);
        controller.update(new SelectionColumnAndOrderAction(view0.getNickname(), 0, new ArrayList<>(List.of(0, 1))));

        assertFalse(view0.isError());
    }
}

