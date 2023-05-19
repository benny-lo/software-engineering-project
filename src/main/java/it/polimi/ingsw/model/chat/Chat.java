package it.polimi.ingsw.model.chat;

import java.util.Stack;

public class Chat implements ChatInterface {
    private final Stack<Message> messages;

    public Chat() {
        messages = new Stack<>();
    }

    @Override
    public void addMessage(String nickname, String text) {
        Message message = new Message(nickname, text);
        messages.add(message);
    }

    @Override
    public Message getLastMessage() {
        return messages.peek();
    }
}
