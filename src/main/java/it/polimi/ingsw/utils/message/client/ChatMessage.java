package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class ChatMessage extends Message {
    private final String text;
    private final String receiver;

    public ChatMessage(String text, String receiver) {
        super();
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
