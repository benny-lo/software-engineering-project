package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.view.server.VirtualView;
import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.RemoteException;

/**
 * Class implementing the {@code ConnectionEstablishmentRMIInterface}.
 */
public class ConnectionEstablishmentRMI implements ConnectionEstablishmentRMIInterface {
    /**
     * {@inheritDoc}
     * Moreover, it creates the {@code ServerConnectionRMI} object (and starts its timer) and
     * the {@code VirtualView} representing the connected client in the server.
     * @param clientConnectionRMIInterface Rmi interface of the client.
     * @return {@code ServerConnectionRMIInterface} interface representing the connection to the server.
     * @throws RemoteException Rmi exception.
     */
    @Override
    public ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException {
        ServerConnectionRMI serverConnection = new ServerConnectionRMI(clientConnectionRMIInterface);
        VirtualView view = new VirtualView(serverConnection);
        serverConnection.setServerInputViewInterface(view);
        serverConnection.start();
        return serverConnection;
    }
}
