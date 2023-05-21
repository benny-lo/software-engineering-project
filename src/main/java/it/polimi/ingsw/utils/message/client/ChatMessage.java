package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class ChatMessage extends Message {
    private final String text;

    public ChatMessage(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
