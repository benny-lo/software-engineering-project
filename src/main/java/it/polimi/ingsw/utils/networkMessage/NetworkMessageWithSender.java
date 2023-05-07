package it.polimi.ingsw.utils.networkMessage;

public abstract class NetworkMessageWithSender extends NetworkMessage {
    private final String nickname;

    public NetworkMessageWithSender(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
