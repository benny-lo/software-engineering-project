package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.VirtualView;

/**
 * A player joins the game.
 */
public class JoinAction extends Action {
    private final VirtualView view;

    public JoinAction(String nickname, VirtualView view) {
        super(nickname);
        this.view = view;
    }

    public VirtualView getView() {
        return view;
    }
}
