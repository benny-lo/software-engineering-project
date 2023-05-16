package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.view.change.ChatListener;

public interface ChatInterface {
    void addMessage(String nickname, String text);
    void setChatListener(ChatListener rep);
}
