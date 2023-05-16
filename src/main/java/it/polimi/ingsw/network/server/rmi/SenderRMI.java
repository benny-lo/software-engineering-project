package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;
import it.polimi.ingsw.network.server.Sender;
import it.polimi.ingsw.utils.networkMessage.server.*;

import java.rmi.RemoteException;

public class SenderRMI implements Sender {
    private final ClientConnectionRMIInterface client;

    public SenderRMI(ClientConnectionRMIInterface client) {
        this.client = client;
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) {
        try {
            client.sendLivingRoomUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) {
        try {
            client.sendBookshelfUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) {
        try {
            client.sendWaitingUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) {
        try {
            client.sendScoresUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) {
        try {
            client.sendEndingTokenUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardsUpdate update) {
        try {
            client.sendCommonGoalCardUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) {
        try {
            client.sendPersonalGoalCardUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendChatUpdate(ChatUpdate update) {
        try {
            client.sendChatUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) {
        try {
            client.sendStartTurnUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) {
        try {
            client.sendEndGameUpdate(update);
        } catch(RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendListOfGames(GamesList gamesList) {
        try {
            client.sendListOfGames(gamesList);
        } catch (RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendItemsSelected(ItemsSelected itemsSelected) {
        try {
            client.sendItemsSelected(itemsSelected);
        } catch (RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }

    @Override
    public void sendAcceptedAction(AcceptedAction acceptedAction) {
        try {
            client.sendAcceptedAction(acceptedAction);
        } catch (RemoteException e) {
            System.err.println("RMI connection with client failed");
        }
    }
}
