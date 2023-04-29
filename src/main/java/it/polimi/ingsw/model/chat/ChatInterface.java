package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.view.rep.ChatRep;

public interface ChatInterface {
    void addMessage(String nickname, String text);
    void setChatListener(ChatRep rep);
}
