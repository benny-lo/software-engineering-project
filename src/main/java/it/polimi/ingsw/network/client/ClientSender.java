package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utils.networkMessage.client.*;

public interface ClientSender {
    void login(Nickname message);
    void createGame(GameInitialization message);
    void selectGame(GameSelection message);
    void selectFromLivingRoom(LivingRoomSelection message);
    void insertInBookshelf(BookshelfInsertion message);
    void writeChat(ChatMessage message);
}
