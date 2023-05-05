package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

public class ChatMessage extends NetworkMessageWithSender {
    private final String text;

    public ChatMessage(String nickname, String text) {
        super(nickname);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
