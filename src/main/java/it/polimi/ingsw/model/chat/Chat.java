package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.view.modelListener.ChatListener;

import java.util.ArrayList;
import java.util.List;

public class Chat implements ChatInterface {
    private final List<Message> messages;
    private final List<ChatListener> chatReps;

    public Chat() {
        messages = new ArrayList<>();
        chatReps = new ArrayList<>();
    }

    @Override
    public void addMessage(String nickname, String text) {
        Message message = new Message(nickname, text);
        messages.add(message);
        for(ChatListener rep : chatReps) {
            rep.updateState(message);
        }
    }

    @Override
    public void setChatListener(ChatListener rep) {
        chatReps.add(rep);
        for(Message message : messages) {
            rep.updateState(message);
        }
    }
}
