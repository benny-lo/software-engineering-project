package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.server.Lobby;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.RemoteException;

public class ConnectionEstablishmentRMI implements ConnectionEstablishmentRMIInterface {
    private final Lobby lobby;
    public ConnectionEstablishmentRMI(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    public ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        VirtualView view = new VirtualViewRMI(lobby, clientConnectionRMIInterface);
        lobby.addVirtualView(view);
        return new ServerConnectionRMI(view);
    }
}
