package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

public class ChatUpdate extends Message {
    private final String nickname;
    private final String text;

    public ChatUpdate(String nickname, String text) {
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
