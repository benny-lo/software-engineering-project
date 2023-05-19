package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class Nickname extends NetworkMessage {
    private final String nickname;
    public Nickname(String nickname) {
        super();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
