package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;
import it.polimi.ingsw.view.UpdateViewInterface;

/**
 * This class represents the connection to a client. VirtualViews are instantiated once per client.
 * It listens for message from the client and notifies the lobby/controller.
 * It is used by the lobby and the controller to send messages to client.
 */
public class VirtualView implements UpdateViewInterface, InputViewInterface {
    /**
     * The nickname chosen by the client represented by {@code this}.
     */
    private String nickname;

    /**
     * Reference to the Controller, this VirtualView is logged in. Initially, it set to {@code null}.
     */
    private Controller controller;

    private ServerConnection serverConnection;

    /**
     * The constructor of VirtualView. It only sets the {@code ServerConnection} and
     * leaves {@code nickname} and {@code controller} to {@code null}.
     */
    public VirtualView(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * The constructor of VirtualView, exclusively used for testing.
     */
    public VirtualView(){}
    /**
     * Setter for the private attribute {@code nickname}.
     * @param nickname the nickname to set.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for the private attribute {@code nickname}.
     * @return a {@code String} corresponding to the nickname of {@code this}.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Setter for the private attribute {@code controller}.
     * @param controller the controller to set.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Method to check whether {@code this} is logged in (has a nickname).
     * @return {@code true} iff the private attribute {@code nickname} is not {@code null}.
     */
    public boolean isLoggedIn() {
        return nickname != null;
    }

    /**
     * Method to check whether {@code this} is logged in any game (has a controller).
     * @return {@code true} iff the private attribute {@code controller} is not {@code null}.
     */
    public boolean isInGame() {
        return controller != null;
    }


    @Override
    public void login(Nickname message) {
        if (controller != null) {
            onGamesList(new GamesList(null));
            return;
        }
        Lobby.getInstance().login(message.getNickname(), this);
    }

    @Override
    public void createGame(GameInitialization message) {
        if (controller != null) {
            onGamesList(new GamesList(null));
            return;
        }
        Lobby.getInstance().createGame(message.getNumberPlayers(), message.getNumberCommonGoalCards(), this);
    }

    @Override
    public void selectGame(GameSelection message) {
        if (controller != null) {
            onGameDimensions(new GameDimensions(-1, -1, -1, -1));
            return;
        }
        Lobby.getInstance().selectGame(message.getId(), this);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) {
        if (controller == null) {
            onItemsSelected(new ItemsSelected(null));
            return;
        }

        controller.update(new SelectionFromLivingRoomAction(this, message.getPositions()));
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) {
        if (controller == null) {
            onAcceptedInsertion(new AcceptedInsertion(false));
            return;
        }

        controller.update(new SelectionColumnAndOrderAction(this, message.getColumn(), message.getPermutation()));
    }

    @Override
    public void writeChat(ChatMessage message) {
        if (controller == null) {
            onChatAccepted(new ChatAccepted(false));
            return;
        }

        controller.update(new ChatMessageAction(this, message.getText()));
    }

    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onWaitingUpdate(WaitingUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onScoresUpdate(ScoresUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onEndingTokenUpdate(EndingTokenUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onChatUpdate(ChatUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onEndGameUpdate(EndGameUpdate update) {
        serverConnection.send(update);
    }

    @Override
    public void onGamesList(GamesList gamesList) {
        serverConnection.send(gamesList);
    }

    @Override
    public void onItemsSelected(ItemsSelected itemsSelected) {
        serverConnection.send(itemsSelected);
    }

    @Override
    public void onAcceptedInsertion(AcceptedInsertion message) {
        serverConnection.send(message);
    }

    @Override
    public void onGameDimensions(GameDimensions message){
        serverConnection.send(message);
    }

    @Override
    public void onChatAccepted(ChatAccepted message) {
        serverConnection.send(message);
    }
}
