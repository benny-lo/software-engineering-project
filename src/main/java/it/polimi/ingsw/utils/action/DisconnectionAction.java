package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

public class DisconnectionAction extends Action {
    /**
     * Constructor of the class
     * @param view - view of the player that disconnects.
     */
    public DisconnectionAction(ServerUpdateViewInterface view) {
        super(view);
    }
}
