package it.polimi.ingsw.network.client.rmi;

import it.polimi.ingsw.network.ServerSettings;
import it.polimi.ingsw.network.server.rmi.ConnectionRMIInterface;
import it.polimi.ingsw.utils.networkMessage.server.*;
import it.polimi.ingsw.view.ClientView;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements ClientRMIInterface, Serializable {
    private ClientView view;
    private ConnectionRMIInterface connectionRMIInterface;

    public ClientRMI() {
        try{
            //TODO: interaction between this and ClientView
            Registry registry = LocateRegistry.getRegistry(ServerSettings.getHostName(), ServerSettings.getRmiPort());
            connectionRMIInterface = (ConnectionRMIInterface) registry.lookup("ServerRMIInterface");
            connectionRMIInterface.login("nickname_here", this);
            System.out.println("ClientRMI is connected.");
        }
        catch (Exception e){
            System.err.println("ClientRMI error");
            e.printStackTrace();
        }
    }

    @Override
    public void sendLivingRoomUpdate(LivingRoomUpdate update) throws RemoteException {

    }

    @Override
    public void sendBookshelfUpdate(BookshelfUpdate update) throws RemoteException {

    }

    @Override
    public void sendWaitingUpdate(WaitingUpdate update) throws RemoteException {

    }

    @Override
    public void sendScoresUpdate(ScoresUpdate update) throws RemoteException {

    }

    @Override
    public void sendEndingTokenUpdate(EndingTokenUpdate update) throws RemoteException {

    }

    @Override
    public void sendCommonGoalCardUpdate(CommonGoalCardUpdate update) throws RemoteException {

    }

    @Override
    public void sendPersonalGoalCardUpdate(PersonalGoalCardUpdate update) throws RemoteException {

    }

    @Override
    public void sendChatUpdate(ChatUpdate update) throws RemoteException {

    }

    @Override
    public void sendStartTurnUpdate(StartTurnUpdate update) throws RemoteException {

    }

    @Override
    public void sendEndGameUpdate(EndGameUpdate update) throws RemoteException {

    }
}
