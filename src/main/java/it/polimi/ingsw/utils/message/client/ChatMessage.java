package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the client representing a message in the chat.
 */
public class ChatMessage extends Message {
    private final String text;
    private final String receiver;

    /**
     * Constructor for the class, It sets the content of the message and the receiver.
     * @param text The text of the chat message.
     * @param receiver The receiver of the chat message.
     */
    public ChatMessage(String text, String receiver) {
        super();
        this.text = text;
        this.receiver = receiver;
    }

    /**
     * Getter of the text.
     * @return The text of the chat message.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter of the receiver.
     * @return The receiver of the chat message.
     */
    public String getReceiver() {
        return receiver;
    }
}
