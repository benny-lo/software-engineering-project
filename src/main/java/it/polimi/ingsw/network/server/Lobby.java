package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.networkMessage.server.GameInfo;
import it.polimi.ingsw.utils.networkMessage.server.GamesList;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * List of virtual views, representing all players connected either waiting for a game or in game.
     */
    private final List<VirtualView> views;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    public Lobby() {
        controllers = new HashMap<>();
        views = new ArrayList<>();
        availableId = 0;
    }

    private List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            if (controllers.get(id).isStarted())   //If a game has already started, it's not displayed.
                ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    private void addController(Controller controller){
        controllers.put(availableId, controller);
        availableId++;
    }

    private VirtualView getView(String nickname) {
        for(VirtualView view : views) {
            if (view.getNickname().equals(nickname)) return view;
        }
        return null;
    }

    public synchronized void login(String nickname, Sender client) {
        if(getView(nickname) != null) {
            client.sendListOfGames(new GamesList(null));
            return;
            // TODO: consider the case in which view is present because client was disconnected and is reconnecting.
        }

        VirtualView view = new VirtualView(nickname);
        view.setToClient(client);
        views.add(view);

        view.sendListOfGames(getGameInfo());
    }

    public synchronized void createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        VirtualView view = getView(nickname);
        if (view == null) return;

        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.sendAcceptedAction(false, "INIT_GAME");
            return;
        }

        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.update(new JoinAction(nickname, view));

        if (view.isError()) {
            view.sendAcceptedAction(false, "INIT_GAME");
        }

        view.setController(controller);
        addController(controller);

        view.sendAcceptedAction(true, "INIT_GAME");
    }


    public synchronized void selectGame(String nickname, int id) {
        VirtualView view = getView(nickname);
        if (view == null) return;

        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.sendAcceptedAction(false, "SELECT_GAME");
            return;
        }

        controllers.get(id).update(new JoinAction(nickname, view));
        boolean error = view.isError();

        if (!error) {
            view.setController(controllers.get(id));
        }

        view.sendAcceptedAction(!view.isError(), "SELECT_GAME");
    }

    public synchronized void selectFromLivingRoom(String nickname, List<Position> positions) {
        VirtualView view = getView(nickname);
        if (view == null) {
            return;
        }

        Controller controller = view.getController();
        if (controller == null) {
            view.sendAcceptedAction(false, "SELECT_LIVINGROOM");
            return;
        }

        controller.update(new SelectionFromLivingRoomAction(nickname, positions));

        if (view.isError()) view.sendAcceptedAction(false, "SELECT_LIVINGROOM");
    }

    public synchronized void putInBookshelf(String nickname, int column, List<Integer> permutation) {
        VirtualView view = getView(nickname);
        if (view == null) return;

        Controller controller = view.getController();
        if (controller == null) {
            view.sendAcceptedAction(false, "INSERT_BOOKSHELF");
            return;
        }

        controller.update(new SelectionColumnAndOrderAction(nickname, column, permutation));

        view.sendAcceptedAction(!view.isError(), "INSERT_BOOKSHELF");
    }

    public synchronized void addMessage(String nickname, String text) {
        VirtualView view = getView(nickname);
        if (view == null) return;

        Controller controller = view.getController();
        if (controller == null) {
            view.sendAcceptedAction(false, "WRITE_CHAT");
            return;
        }

        controller.update(new ChatMessageAction(nickname, text));

        view.sendAcceptedAction(!view.isError(), "WRITE_CHAT");
    }
}
