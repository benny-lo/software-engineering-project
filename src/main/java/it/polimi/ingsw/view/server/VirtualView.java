package it.polimi.ingsw.view.server;

import it.polimi.ingsw.controller.ControllerInterface;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.*;


/**
 * This class represents a client on the server-side. {@code VirtualView}s are instantiated once per client.
 * It listens for message from the client (coming from the network) and notifies the {@code Lobby}/{@code Controller}.
 * It is used by the {@code Lobby} and the {@code Controller} to send messages to the client.
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
    private ControllerInterface controller;
    private final ServerConnection serverConnection;
    private final Object nicknameLock;
    private final Object controllerLock;

    /**
     * The constructor of {@code VirtualView}. It only sets the {@code ServerConnection} and
     * leaves {@code nickname} and {@code controller} to {@code null}.
     * @param serverConnection The connection to the client {@code this} represents.
     */
    public VirtualView(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.nicknameLock = new Object();
        this.controllerLock = new Object();
    }

    /**
     * {@inheritDoc}
     * THREAD-SAFE synchronizing privately.
     * @param nickname The nickname to set.
     */
    @Override
    public void setNickname(String nickname) {
        synchronized (nicknameLock) {
            this.nickname = nickname;
        }
    }

    /**
     * {@inheritDoc}
     * THREAD-SAFE synchronizing privately.
     * @return {@code String} corresponding to the nickname of {@code this}.
     */
    @Override
    public String getNickname() {
        synchronized (nicknameLock) {
            return nickname;
        }
    }

    /**
     * {@inheritDoc}
     * THREAD-SAFE synchronizing privately.
     * @param controller The {@code Controller} joined.
     */
    @Override
    public void setController(ControllerInterface controller) {
        synchronized (controllerLock) {
            this.controller = controller;
        }
    }

    /**
     * {@inheritDoc}
     * THREAD-SAFE synchronizing privately.
     * @return {@code true} iff the {@code Controller} of {@code this} is not {@code null}.
     */
    @Override
    public boolean isInGame() {
        synchronized (controllerLock) {
            return controller != null;
        }
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally, it requires the lock on {@code Lobby}.
     * @param message Message containing the chosen nickname.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally, it requires the lock on {@code Lobby}.
     * @param message Message containing the information about the game to create.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally it requires the lock on {@code Lobby}.
     * @param message Message containing the id of the game chosen.
     */
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

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally it requires the lock on the assigned {@code Controller}.
     * @param message Message containing the chosen positions.
     */
    @Override
    public synchronized void selectFromLivingRoom(LivingRoomSelection message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onSelectedItems(new SelectedItems(null));
                return;
            }
        }
        controller.livingRoom(message.getPositions(), this);
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally it requires the lock on the assigned {@code Controller}.
     * @param message Message containing the column and the order in which to insert the chosen tiles.
     */
    @Override
    public synchronized void insertInBookshelf(BookshelfInsertion message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onAcceptedInsertion(new AcceptedInsertion(false));
                return;
            }
        }
        controller.bookshelf(message.getColumn(), message.getPermutation(), this);
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally it requires the lock on the assigned {@code Controller}.
     * @param message Message containing the text written.
     */
    @Override
    public synchronized void writeChat(ChatMessage message) {
        synchronized (controllerLock) {
            if (controller == null) {
                onChatAccepted(new ChatAccepted(false));
                return;
            }
        }

        controller.chat(message.getText(), message.getReceiver(), this);
    }

    /**
     * {@inheritDoc}
     * It synchronizes on {@code this}. Internally it requires the lock on the assigned {@code Controller} and
     * on {@code Lobby} in a non-nested way.
     */
    @Override
    public synchronized void disconnect() {
        synchronized (nicknameLock) {
            if (nickname != null) {
                Logger.logout(nickname);
            } else {
                Logger.logout();
            }
        }


        boolean flag;
        synchronized (controllerLock) {
            flag = (controller != null);
        }
        if (flag) {
            controller.disconnection(this);
            if (controller.getNumberActualPlayers() < 1) Lobby.getInstance().removeController(controller);
        }
        Lobby.getInstance().removeServerUpdateViewInterface(this);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onLivingRoomUpdate(LivingRoomUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onBookshelfUpdate(BookshelfUpdate update) {
       serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onWaitingUpdate(WaitingUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onScoresUpdate(ScoresUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onEndingTokenUpdate(EndingTokenUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onChatUpdate(ChatUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onStartTurnUpdate(StartTurnUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param update The update to process.
     */
    @Override
    public void onEndGameUpdate(EndGameUpdate update) {
        serverConnection.send(update);
    }

    /**
     * {@inheritDoc}
     * @param gamesList The message to process.
     */
    @Override
    public void onGamesList(GamesList gamesList) {
        serverConnection.send(gamesList);
    }

    /**
     * {@inheritDoc}
     * @param selectedItems The message to process.
     */
    @Override
    public void onSelectedItems(SelectedItems selectedItems) {
        serverConnection.send(selectedItems);
    }

    /**
     * {@inheritDoc}
     * @param message The message to process.
     */
    @Override
    public void onAcceptedInsertion(AcceptedInsertion message) {
        serverConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message The message to process.
     */
    @Override
    public void onGameData(GameData message){
        serverConnection.send(message);
    }

    /**
     * {@inheritDoc}
     * @param message The message to process.
     */
    @Override
    public void onChatAccepted(ChatAccepted message) {
        serverConnection.send(message);
    }
}
