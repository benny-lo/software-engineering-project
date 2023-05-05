package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

public class CommonGoalCardUpdate extends NetworkMessage {
    private final int id;
    private final int top;

    public CommonGoalCardUpdate(int id, int top) {
        this.id = id;
        this.top = top;
    }

    public int getId() {
        return id;
    }

    public int getTop() {
        return top;
    }
}
