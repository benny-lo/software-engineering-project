package it.polimi.ingsw.model.chat;

public interface ChatInterface {
    void addMessage(String nickname, String text);
    Message getLastMessage();
}
