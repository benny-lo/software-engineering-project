package it.polimi.ingsw.utils.action;

import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

public class ChatMessageAction extends Action {
    private final String text;
    private final String receiver;

    /**
     * Constructor of the class
     * @param view - view of the player that sent the message
     * @param text - the text of the chat message
     * @param receiver - the receiver to which the message is sent.
     */
    public ChatMessageAction(ServerUpdateViewInterface view, String text, String receiver) {
        super(view);
        this.text = text;
        this.receiver = receiver;
    }

    /**
     * Getter for the text of the message
     * @return - the text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for the receiver of the message
     * @return - the receiver of the message.
     */
    public String getReceiver() {
        return receiver;
    }
}
