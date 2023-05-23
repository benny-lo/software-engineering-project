package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.VirtualView;

public class ChatMessageAction extends Action {
    private final String text;
    private final String receiver;

    public ChatMessageAction(VirtualView view, String text, String receiver) {
        super(view);
        this.text = text;
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public String getReceiver() {
        return receiver;
    }
}
