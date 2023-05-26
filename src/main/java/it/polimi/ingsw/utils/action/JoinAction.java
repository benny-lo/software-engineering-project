package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

/**
 * A player joins the game.
 */
public class JoinAction extends Action {
    /**
     * Constructor for the class.
     * @param view - view of the player that joins the game
     */
    public JoinAction(ServerUpdateViewInterface view) {
        super(view);
    }
}
