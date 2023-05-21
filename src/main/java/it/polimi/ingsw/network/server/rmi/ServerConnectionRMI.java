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

    @Override
    public void send(LivingRoomUpdate update) {
        try {
            client.sendLivingRoomUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(BookshelfUpdate update) {
        try {
            client.sendBookshelfUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(WaitingUpdate update) {
        try {
            client.sendWaitingUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ScoresUpdate update) {
        try {
            client.sendScoresUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(EndingTokenUpdate update) {
        try {
            client.sendEndingTokenUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(CommonGoalCardsUpdate update) {
        try {
            client.sendCommonGoalCardUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(PersonalGoalCardUpdate update) {
        try {
            client.sendPersonalGoalCardUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ChatUpdate update) {
        try {
            client.sendChatUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(StartTurnUpdate update) {
        try {
            client.sendStartTurnUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(EndGameUpdate update) {
        try {
            client.sendEndGameUpdate(update);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(GamesList gamesList) {
        try {
            client.sendListOfGames(gamesList);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ItemsSelected itemsSelected) {
        try {
            client.sendItemsSelected(itemsSelected);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(GameDimensions gameDimensions) {
        try {
            client.sendGameDimensions(gameDimensions);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(AcceptedInsertion acceptedInsertion) {
        try {
            client.sendAcceptedInsertion(acceptedInsertion);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(ChatAccepted chatAccepted) {
        try {
            client.sendChatAccepted(chatAccepted);
        } catch (RemoteException e) {
            throw  new RuntimeException(e);
        }
    }
}
