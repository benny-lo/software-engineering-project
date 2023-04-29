package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.view.rep.ChatRep;

import java.util.ArrayList;
import java.util.List;

public class Chat implements ChatInterface {
    private final List<Message> messages;
    private final List<ChatRep> chatReps;

    public Chat() {
        messages = new ArrayList<>();
        chatReps = new ArrayList<>();
    }

    @Override
    public void addMessage(String nickname, String text) {
        Message message = new Message(nickname, text);
        messages.add(message);
        for(ChatRep rep : chatReps) {
            rep.updateRep(message);
        }
    }

    @Override
    public void setChatListener(ChatRep rep) {
        chatReps.add(rep);
        for(Message message : messages) {
            rep.updateRep(message);
        }
    }
}
