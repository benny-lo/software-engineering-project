package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.message.client.*;

public interface ClientConnection {
    void send(Nickname message);
    void send(GameInitialization message);
    void send(GameSelection message);
    void send(LivingRoomSelection message);
    void send(BookshelfInsertion message);
    void send(ChatMessage message);
}
