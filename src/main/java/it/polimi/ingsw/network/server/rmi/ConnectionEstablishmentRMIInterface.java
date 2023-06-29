package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface to export through rmi. It allows the client to start a rmi connection to the server.
 */
public interface ConnectionEstablishmentRMIInterface extends Remote {
    /**
     * Initializes the connection to server.
     * @param clientConnectionRMIInterface Rmi interface of the client.
     * @return {@code ServerConnectionRMIInterface} interface representing the connection to the server.
     * @throws RemoteException Rmi exception.
     */
    ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException;
}
