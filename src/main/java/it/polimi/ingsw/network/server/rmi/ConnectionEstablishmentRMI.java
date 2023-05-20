package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.controller.Lobby;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.RemoteException;

public class ConnectionEstablishmentRMI implements ConnectionEstablishmentRMIInterface {
    private final Lobby lobby;
    public ConnectionEstablishmentRMI(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        ServerConnectionRMI serverConnection = new ServerConnectionRMI(clientConnectionRMIInterface);
        VirtualView view = new VirtualViewRMI(lobby, serverConnection);
        serverConnection.setReceiver(view);
        lobby.addVirtualView(view);
        return serverConnection;
    }
}
