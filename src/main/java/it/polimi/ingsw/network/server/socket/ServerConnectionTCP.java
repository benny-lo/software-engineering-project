package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.network.server.Sender;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.Receiver;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;

public class ServerConnectionTCP implements Receiver {
    private final Lobby lobby;

    public ServerConnectionTCP(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void receive(Object object, Sender sender) {
        if (object instanceof Nickname) {
            lobby.login(
                    ((Nickname) object).getNickname(), sender
            );
        } else if (object instanceof GameSelection) {
            lobby.selectGame(
                    ((GameSelection) object).getNickname(),
                    ((GameSelection) object).getId()
            );
        } else if (object instanceof GameInitialization) {
            lobby.createGame(
                    ((GameInitialization) object).getNickname(),
                    ((GameInitialization) object).getNumberPlayers(),
                    ((GameInitialization) object).getNumberCommonGoalCards()
                    );
        } else if (object instanceof LivingRoomSelection) {
            lobby.selectFromLivingRoom(
                    ((LivingRoomSelection) object).getNickname(),
                    ((LivingRoomSelection) object).getPositions()
            );
        } else if (object instanceof BookshelfInsertion) {
            lobby.putInBookshelf(
                    ((BookshelfInsertion) object).getNickname(),
                    ((BookshelfInsertion) object).getColumn(),
                    ((BookshelfInsertion) object).getPermutation()
            );
        } else if (object instanceof ChatUpdate) {
            lobby.addMessage(
                    ((ChatUpdate) object).getNickname(),
                    ((ChatUpdate) object).getText()
            );
        }
    }
}
