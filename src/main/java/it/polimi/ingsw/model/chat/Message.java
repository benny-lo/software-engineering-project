package it.polimi.ingsw.model.chat;

public class Message {
    private final String nickname;
    private final String text;

    public Message(String nickname, String text) {
        this.nickname = nickname;
        this.text = text;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }
}
