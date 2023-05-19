package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.server.ServerReceiver;
import it.polimi.ingsw.network.server.ServerSender;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.networkMessage.client.*;
import it.polimi.ingsw.utils.networkMessage.server.AcceptedAction;
import it.polimi.ingsw.utils.networkMessage.server.AcceptedActionTypes;
import it.polimi.ingsw.utils.networkMessage.server.GamesList;
import it.polimi.ingsw.utils.networkMessage.server.ItemsSelected;

public abstract class VirtualView implements ServerSender, ServerReceiver {
    private String nickname;
    private final Lobby lobby;
    private Controller controller;

    public VirtualView(Lobby lobby) {
        this.lobby = lobby;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void login(Nickname message) {
        if (controller != null) {
            sendListOfGames(new GamesList(null));
            return;
        }
        lobby.login(message.getNickname(), this);
    }

    @Override
    public void createGame(GameInitialization message) {
        if (controller != null) {
            sendAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.CREATE_GAME));
            return;
        }
        lobby.createGame(message.getNumberPlayers(), message.getNumberCommonGoalCards(), this);
    }

    @Override
    public void selectGame(GameSelection message) {
        if (controller != null) {
            sendAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.SELECT_GAME));
            return;
        }
        lobby.selectGame(message.getId(), this);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        if (controller == null) {
            sendItemsSelected(new ItemsSelected(null));
            return;
        }

        controller.update(new SelectionFromLivingRoomAction(nickname, message.getPositions()));
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        if (controller == null) {
            sendAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.INSERT_BOOKSHELF));
            return;
        }

        controller.update(new SelectionColumnAndOrderAction(nickname, message.getColumn(), message.getPermutation()));
    }

    @Override
    public void writeChat(ChatMessage message) {
        if (controller == null) {
            sendAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.WRITE_CHAT));
            return;
        }

        controller.update(new ChatMessageAction(nickname, message.getText()));
    }
}
