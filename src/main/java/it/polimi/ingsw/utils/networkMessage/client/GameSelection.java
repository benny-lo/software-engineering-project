package it.polimi.ingsw.utils.networkMessage.client;

import it.polimi.ingsw.utils.networkMessage.NetworkMessageWithSender;

public class GameSelection extends NetworkMessageWithSender {
    private final int id;

    public GameSelection(String nickname, int id) {
        super(nickname);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
