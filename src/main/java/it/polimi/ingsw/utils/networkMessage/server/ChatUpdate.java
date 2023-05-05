package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class ChatUpdate extends NetworkMessage {
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
