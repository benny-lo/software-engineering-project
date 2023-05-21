package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

/**
 * Abstract class representing a general action that the client may perform, while the game is being played.
 */
public abstract class Action {
    VirtualView view;
    public Action(VirtualView view) {
        this.view = view;
    }

    public VirtualView getView() {
        return view;
    }
}
