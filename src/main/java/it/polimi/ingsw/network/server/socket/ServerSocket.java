package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.Receiver;
import it.polimi.ingsw.utils.networkMessage.client.Nickname;

public class ServerSocket implements Receiver {
    private final Lobby lobby;

    public ServerSocket(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void receive(Object object) {
        if (object instanceof Nickname) {

        }
    }
}
