package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utils.message.client.*;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.InputViewInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerConnectionRMI extends UnicastRemoteObject implements ServerConnection, ServerConnectionRMIInterface {
    private InputViewInterface receiver;
    private final ClientConnectionRMIInterface client;

    public ServerConnectionRMI(ClientConnectionRMIInterface client) throws RemoteException {
        super();
        this.client = client;
    }

    public void setInputViewInterface(InputViewInterface receiver) {
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

    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        try {
            client.sendLivingRoomUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendBookshelfUpdate(BookshelfUpdate update) {
        try {
            client.sendBookshelfUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendWaitingUpdate(WaitingUpdate update) {
        try {
            client.sendWaitingUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendScoresUpdate(ScoresUpdate update) {
        try {
            client.sendScoresUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        try {
            client.sendEndingTokenUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommonGoalCardsUpdate(CommonGoalCardsUpdate update) {
        try {
            client.sendCommonGoalCardUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        try {
            client.sendPersonalGoalCardUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChatUpdate(ChatUpdate update) {
        try {
            client.sendChatUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendStartTurnUpdate(StartTurnUpdate update) {
        try {
            client.sendStartTurnUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEndGameUpdate(EndGameUpdate update) {
        try {
            client.sendEndGameUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendGamesList(GamesList gamesList) {
        try {
            client.sendListOfGames(gamesList);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendItemsSelected(ItemsSelected itemsSelected) {
        try {
            client.sendItemsSelected(itemsSelected);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendAcceptedAction(AcceptedAction acceptedAction) {
        try {
            client.sendAcceptedAction(acceptedAction);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendLivingRoomAndBookshelvesDimensions(LivingRoomAndBookshelvesDimensions livingRoomAndBookshelvesDimensions) {
        try {
            client.sendLivingRoomAndBookshelvesDimensions(livingRoomAndBookshelvesDimensions);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
