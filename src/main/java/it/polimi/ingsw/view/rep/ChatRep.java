package it.polimi.ingsw.view.rep;

import it.polimi.ingsw.model.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatRep extends Rep {
    private final List<Message> needSend;

    public ChatRep() {
        this.needSend = new ArrayList<>();
    }

    public List<Message> getChat() {
        peek();
        List<Message> copy = new ArrayList<>(needSend);
        needSend.clear();
        return copy;
    }

    public void updateRep(Message message) {
        update();
        needSend.add(message);
    }
}
