package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server when updates to the chat happen.
 */
public class ChatUpdate extends Message {
    private final String sender;
    private final String text;
    private final String receiver;

    /**
     * Constructor for the class. It sets the sender, text and receiver.
     * @param sender The sender of the chat update.
     * @param text The text of the chat update.
     * @param receiver The receiver of the chat update.
     */
    public ChatUpdate(String sender, String text, String receiver) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver;
    }

    /**
     * Getter for the sender.
     * @return The sender of the chat update.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter for the text.
     * @return The text of the chat update.
     */
    public String getText() {
        return text;
    }
    /**
     * Getter for the receiver.
     * @return The receiver of the chat update.
     */
    public String getReceiver() {
        return receiver;
    }
}
