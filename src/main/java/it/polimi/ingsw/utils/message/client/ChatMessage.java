package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class ChatMessage extends Message {
    private final String text;
    private final String receiver;

    /**
     * Constructor for the class
     * @param text - text of the chat message
     * @param receiver - reciever of the chat message
     */
    public ChatMessage(String text, String receiver) {
        super();
        this.text = text;
        this.receiver = receiver;
    }

    /**
     * Getter of the text
     * @return - the text of the chat message
     */
    public String getText() {
        return text;
    }

    /**
     * Getter of the receiver
     * @return - the receiver of the chat message
     */
    public String getReceiver() {
        return receiver;
    }
}
