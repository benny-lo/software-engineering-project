package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

/**
 * A player joins the game.
 */
public class JoinAction extends Action {
    /**
     * Constructor for the class.
     * @param view - view of the player that joins the game
     */
    public JoinAction(VirtualView view) {
        super(view);
    }
}
