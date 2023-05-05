package it.polimi.ingsw.utils.networkMessage;

public abstract class NetworkMessageWithSender extends NetworkMessage {
    private final String nickname;

    public NetworkMessageWithSender() {
        nickname = null;
    }

    public NetworkMessageWithSender(String nickname) {
        super();
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
