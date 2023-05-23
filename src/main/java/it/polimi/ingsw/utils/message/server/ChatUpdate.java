package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class ChatUpdate extends Message {
    private final String sender;
    private final String text;
    private final String receiver;

    public ChatUpdate(String sender, String text, String receiver) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public String getReceiver() {
        return receiver;
    }
}
