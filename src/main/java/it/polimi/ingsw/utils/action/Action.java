package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

/**
 * Abstract class representing a general action that the client may perform, while the game is being played.
 */
public abstract class Action {
    ServerUpdateViewInterface view;

    /**
     * Constructor for the class
     * @param view - the view from which the Action is performed from.
     */
    public Action(ServerUpdateViewInterface view) {
        this.view = view;
    }

    /**
     * Getter for the view of the Action
     * @return - the Virtual View of the Action
     */
    public ServerUpdateViewInterface getView() {
        return view;
    }
}
