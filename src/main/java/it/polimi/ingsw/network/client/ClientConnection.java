package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.message.client.*;

public interface ClientConnection {
    void login(Nickname message);
    void createGame(GameInitialization message);
    void selectGame(GameSelection message);
    void selectFromLivingRoom(LivingRoomSelection message);
    void insertInBookshelf(BookshelfInsertion message);
    void writeChat(ChatMessage message);
}
