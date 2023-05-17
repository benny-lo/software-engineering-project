package it.polimi.ingsw.view.modelListener;

import it.polimi.ingsw.model.chat.Message;

public class ChatListener extends ModelListener {
    private Message lastMessage;

    public Message getChat() {
        changed = false;
        Message ret = lastMessage;
        lastMessage = null;

        return ret;
    }

    public void updateState(Message lastMessage) {
        changed = true;
        this.lastMessage = lastMessage;
    }
}
