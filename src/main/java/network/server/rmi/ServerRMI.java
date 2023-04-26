package network.server.rmi;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.GameInfo;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.view.VirtualView;
import network.client.ClientRMIInterface;
import network.server.Lobby;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerRMI extends UnicastRemoteObject implements ServerRMIInterface {
    private final Lobby lobby;
    private VirtualView view;

    public ServerRMI(Lobby lobby) throws RemoteException {
        this.lobby = lobby;
        this.view = null;
    }

    @Override
    public List<GameInfo> login(ClientRMIInterface client, String nickname) {
        for (VirtualView view : lobby.getViews())
            if (view.getNickname().equals(nickname))
                //TODO : notify error in login
                return null;
        view = new VirtualView(nickname);
        lobby.addVirtualView(view);
        return lobby.getGameInfo();
    }

    @Override
    public boolean selectGame(int id) {
        for(GameInfo game : lobby.getGameInfo()){
            if (game.getId() == id && (game.getNumberPlayers() - game.getNumberPlayersSignedIn()) > 0){
                lobby.getControllers().get(id).update(new JoinAction(view.getNickname(), view));
                game.addPlayer();
                return true;
            }
        }
        //TODO : notify error in game selection
        return false;
    }

    @Override
    public boolean createGame(int numberPlayers, int numberCommonGoals) {
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2)
            //TODO : notify error in game creation
            return false;

        //TODO: lobby has to be synchronized, otherwise gameId could change if another client create a game in the same moment
        lobby.addController(numberPlayers, numberCommonGoals);
        lobby.getControllers().get(lobby.getGameId()).update(new JoinAction(view.getNickname(), view));
        lobby.getGameInfo().get(lobby.getGameId()).addPlayer();
        return true;
    }

    @Override
    public List<Item> selectFromLivingRoom(List<Position> position) throws RemoteException {
        return null;
    }

    @Override
    public boolean putInBookshelf(int column, List<Integer> permutation) {
        return false;
    }
}
