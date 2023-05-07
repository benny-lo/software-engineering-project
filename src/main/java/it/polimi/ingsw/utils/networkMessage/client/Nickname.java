package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

public class Nickname extends NetworkMessageWithSender {
    public Nickname(String nickname) {
        super(nickname);
    }
}
