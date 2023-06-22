package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.DisconnectionAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;


/**
 * This class represents the connection to a client. VirtualViews are instantiated once per client.
 * It listens for message from the client and notifies the lobby/controller.
 * It is used by the lobby and the controller to send messages to client.
 * All methods inherited from {@code ServerInputViewInterface} take the lock on {@code this}.
 * All other methods are thread-safe and do not take the lock on {@code this}.
 */
public class VirtualView implements ServerUpdateViewInterface, ServerInputViewInterface {
    /**
     * The nickname chosen by the client represented by {@code this}.
     */
    private String nickname;

    /**
     * Reference to the Controller, this VirtualView is logged in. Initially, it set to {@code null}.
     */
    private Controller controller;
    private final ServerConnection serverConnection;
    private final Object nicknameLock;
    private final Object controllerLock;

    /**
     * The constructor of VirtualView. It only sets the {@code ServerConnection} and
     * leaves {@code nickname} and {@code controller} to {@code null}.
     */
    public VirtualView(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.nicknameLock = new Object();
        this.controllerLock = new Object();
    }

    /**
     * Setter for the private attribute {@code nickname}.
     * @param nickname the nickname to set.
     */
    @Override
    public void setNickname(String nickname) {
        synchronized (nicknameLock) {
            this.nickname = nickname;
        }
    }

    /**
     * Getter for the private attribute {@code nickname}.
     * @return a {@code String} corresponding to the nickname of {@code this}.
     */
    @Override
    public String getNickname() {
        synchronized (nicknameLock) {
            return nickname;
        }
    }

    /**
     * Setter for the private attribute {@code controller}.
     * @param controller the controller to set.
     */
    @Override
    public void setController(Controller controller) {
        synchronized (controllerLock) {
            this.controller = controller;
        }
    }

    /**
     * Method to check whether {@code this} is logged in any game (has a controller).
     * @return {@code true} iff the private attribute {@code controller} is not {@code null}.
     */
    @Override
    public boolean isInGame() {
        synchronized (controllerLock) {
            return controller != null;
        }
    }


    @Override
    public synchronized void login(Nickname message) {
        synchronized (controllerLock) {
            if (controller != null) {
                onGamesList(new GamesList(null));
                return;
            }
        }
        Lobby.getInstance().login(message.getNickname(), this);
    }

    @Override
    public synchronized void createGame(GameInitialization message) {
        synchronized (controllerLock) {
            if (controller != null) {
                onGamesList(new GamesList(null));
                return;
            }
        }
        Lobby.getInstance().createGame(message.getNumberPlayers(), message.getNumberCommonGoalCards(), this);
    }

    @Override
    public synchronized void selectGame(GameSelection message) {
        synchronized (controllerLock) {
            if (controller != null) {
                onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
                return;
            }
        }
        Lobby.getInstance().selectGame(message.getId(), this);
    }

    @Override
    public synchronized void selectFromLivingRoom(LivingRoomSelection message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onSelectedItems(new SelectedItems(null));
                return;
            }
        }
        controller.perform(new SelectionFromLivingRoomAction(this, message.getPositions()));
    }

    @Override
    public synchronized void insertInBookshelf(BookshelfInsertion message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onAcceptedInsertion(new AcceptedInsertion(false));
                return;
            }
        }
        controller.perform(new SelectionColumnAndOrderAction(this, message.getColumn(), message.getPermutation()));
    }

    @Override
    public synchronized void writeChat(ChatMessage message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onChatAccepted(new ChatAccepted(false));
                return;
            }
        }

        controller.perform(new ChatMessageAction(this, message.getText(), message.getReceiver()));
    }

    @Override
    public synchronized void disconnect() {
        synchronized (nicknameLock) {
            if (nickname != null) {
                Logger.logout(nickname);
            } else {
                Logger.logout();
            }
        }

        Lobby.getInstance().removeServerUpdateViewInterface(this);
        boolean flag;
        synchronized (controllerLock) {
            flag = (controller != null);
        }
        if (flag) {
            controller.perform(new DisconnectionAction(this));
            if (controller.getNumberActualPlayers() < 1) Lobby.getInstance().removeController(controller);
        }

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
    public void onSelectedItems(SelectedItems selectedItems) {
        serverConnection.send(selectedItems);
    }

    @Override
    public void onAcceptedInsertion(AcceptedInsertion message) {
        serverConnection.send(message);
    }

    @Override
    public void onGameData(GameData message){
        serverConnection.send(message);
    }

    @Override
    public void onChatAccepted(ChatAccepted message) {
        serverConnection.send(message);
    }
}
