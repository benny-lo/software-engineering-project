package it.polimi.ingsw.utils.message.client;

import it.polimi.ingsw.utils.message.Message;

public class Nickname extends Message {
    private final String nickname;
    public Nickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
