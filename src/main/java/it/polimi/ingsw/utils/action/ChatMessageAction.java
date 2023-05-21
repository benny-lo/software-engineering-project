package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

public class ChatMessageAction extends Action {
    private final String text;

    public ChatMessageAction(VirtualView view, String text) {
        super(view);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
