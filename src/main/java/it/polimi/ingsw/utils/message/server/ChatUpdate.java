package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class ChatUpdate extends Message {
    private final String sender;
    private final String text;
    private final String receiver;

    /**
     * Constructor for the class
     * @param sender - the sender of the chat update
     * @param text - the text of the chat update
     * @param receiver - the receiver of the chat update
     */
    public ChatUpdate(String sender, String text, String receiver) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver;
    }

    /**
     * Getter for the sender
     * @return - the sender of the chat update
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter for the text
     * @return - the text of the chat update
     */
    public String getText() {
        return text;
    }
    /**
     * Getter for the receiver
     * @return - the receiver of the chat update.
     */
    public String getReceiver() {
        return receiver;
    }
}
