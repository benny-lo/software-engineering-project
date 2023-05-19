package it.polimi.ingsw.network.server.rmi;

import it.polimi.ingsw.network.client.rmi.ClientConnectionRMIInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConnectionEstablishmentRMIInterface extends Remote {
    ServerConnectionRMIInterface init(ClientConnectionRMIInterface clientConnectionRMIInterface) throws RemoteException;
}
