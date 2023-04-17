package it.polimi.ingsw.utils.action;

/**
 * Abstract class representing a general action that the client may perform.
 * It is used for communication client -> server.
 */
public abstract class Action {
    String senderNickname;
    public Action(String nickname) {
        this.senderNickname = nickname;
    }

    public String getSenderNickname() {
        return senderNickname;
    }
}
