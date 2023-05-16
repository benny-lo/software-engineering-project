package it.polimi.ingsw.network.server.socket;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.network.server.Sender;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.Receiver;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.util.List;

public class ServerConnectionTCP implements Receiver {
    private final Lobby lobby;

    public ServerConnectionTCP(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public void receive(Object object, Sender sender) {
        if (object instanceof Nickname) {
            List<GameInfo> ret = lobby.login(
                    ((Nickname) object).getNickname(), sender
            );
            sender.sendListOfGames(new GamesList(ret));
        } else if (object instanceof GameSelection) {
            boolean ret = lobby.selectGame(
                    ((GameSelection) object).getNickname(),
                    ((GameSelection) object).getId()
            );
            sender.sendAccepted(new AcceptedAction(ret, "GAME_SELECTION"));
        } else if (object instanceof GameInitialization) {
            boolean ret = lobby.createGame(
                    ((GameInitialization) object).getNickname(),
                    ((GameInitialization) object).getNumberPlayers(),
                    ((GameInitialization) object).getNumberCommonGoalCards()
                    );
            sender.sendAccepted(new AcceptedAction(ret, "GAME_INITIALIZATION"));
        } else if (object instanceof LivingRoomSelection) {
            List<Item> ret = lobby.selectFromLivingRoom(
                    ((LivingRoomSelection) object).getNickname(),
                    ((LivingRoomSelection) object).getPositions()
            );
            sender.sendItemsSelection(new ItemsSelected(ret));
        } else if (object instanceof BookshelfInsertion) {
            boolean ret = lobby.putInBookshelf(
                    ((BookshelfInsertion) object).getNickname(),
                    ((BookshelfInsertion) object).getColumn(),
                    ((BookshelfInsertion) object).getPermutation()
            );
            sender.sendAccepted(new AcceptedAction(ret, "BOOKSHELF_INSERTION"));
        } else if (object instanceof ChatUpdate) {
            boolean ret = lobby.addMessage(
                    ((ChatUpdate) object).getNickname(),
                    ((ChatUpdate) object).getText()
            );
            sender.sendAccepted(new AcceptedAction(ret, "CHAT_WRITE"));
        }
    }
}
