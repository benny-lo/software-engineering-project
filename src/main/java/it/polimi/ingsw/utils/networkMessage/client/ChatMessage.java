package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class ChatMessage extends NetworkMessage {
    private final String text;

    public ChatMessage(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
