package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.RemoteException;

public class ConnectionEstablishmentRMI implements ConnectionEstablishmentRMIInterface {
    @Override
    public ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        ServerConnectionRMI serverConnection = new ServerConnectionRMI(clientConnectionRMIInterface);
        VirtualView view = new VirtualView(serverConnection);
        serverConnection.setServerInputViewInterface(view);
        Lobby.getInstance().addVirtualView(view);
        serverConnection.startTimers();
        return serverConnection;
    }
}
