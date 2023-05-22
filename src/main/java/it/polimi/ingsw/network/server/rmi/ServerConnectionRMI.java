package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.Beep;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerInputViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;

public class ServerConnectionRMI extends UnicastRemoteObject implements ServerConnection, ServerConnectionRMIInterface {
    private ServerInputViewInterface receiver;
    private final ClientConnectionRMIInterface client;
    private final Timer serverTimer;
    private final Timer clientTimer;
    private Beep clientBeep;

    public ServerConnectionRMI(ClientConnectionRMIInterface client) throws RemoteException {
        super();
        this.client = client;
        this.serverTimer = new Timer();
        this.clientTimer = new Timer();
    }

    public void startTimers() {
        serverTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.beep(new Beep());
                } catch (RemoteException e) {
                    receiver.disconnect();
                }
            }
        }, 1000, 1000);

        clientTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (clientBeep != null) {
                    clientBeep = null;
                    return;
                }
                receiver.disconnect();
            }
        }, 1000, 1000);
    }

    @Override
    public void setServerInputViewInterface(ServerInputViewInterface receiver) {
        this.receiver = receiver;
    }

    @Override
    public void login(Nickname message) throws RemoteException {
        receiver.login(message);
    }

    @Override
    public void createGame(GameInitialization message) throws RemoteException {
        receiver.createGame(message);
    }

    @Override
    public void selectGame(GameSelection message) throws RemoteException {
        receiver.selectGame(message);
    }

    @Override
    public void selectFromLivingRoom(LivingRoomSelection message) throws RemoteException {
        receiver.selectFromLivingRoom(message);
    }

    @Override
    public void insertInBookshelf(BookshelfInsertion message) throws RemoteException {
        receiver.insertInBookshelf(message);
    }

    @Override
    public void writeChat(ChatMessage message) throws RemoteException {
        receiver.writeChat(message);
    }

    @Override
    public void beep(Beep beep) throws RemoteException {
        this.clientBeep = beep;
    }

    @Override
    public void send(LivingRoomUpdate update) {
        try {
            client.sendLivingRoomUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(BookshelfUpdate update) {
        try {
            client.sendBookshelfUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(WaitingUpdate update) {
        try {
            client.sendWaitingUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(ScoresUpdate update) {
        try {
            client.sendScoresUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(EndingTokenUpdate update) {
        try {
            client.sendEndingTokenUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        try {
            client.sendCommonGoalCardUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        try {
            client.sendPersonalGoalCardUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(ChatUpdate update) {
        try {
            client.sendChatUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(StartTurnUpdate update) {
        try {
            client.sendStartTurnUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(EndGameUpdate update) {
        try {
            client.sendEndGameUpdate(update);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(GamesList gamesList) {
        try {
            client.sendListOfGames(gamesList);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        try {
            client.sendItemsSelected(itemsSelected);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(GameData gameData) {
        try {
            client.sendGameDimensions(gameData);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        try {
            client.sendAcceptedInsertion(acceptedInsertion);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        try {
            client.sendChatAccepted(chatAccepted);
        } catch (RemoteException e) {
            serverTimer.cancel();
            clientTimer.cancel();
            receiver.disconnect();
        }
    }
}
