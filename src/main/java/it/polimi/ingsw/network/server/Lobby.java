package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.action.ChatMessageAction;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.action.SelectionColumnAndOrderAction;
import it.polimi.ingsw.utils.action.SelectionFromLivingRoomAction;
import it.polimi.ingsw.utils.networkMessage.server.GameInfo;
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

    public synchronized List<GameInfo> login(String nickname, UpdateSender client) {
        if(getView(nickname) != null) {
            return null;
            // TODO: consider the case in which view is present because client was disconnected and is reconnecting.
        }
        VirtualView view = new VirtualView(nickname);
        view.setToClient(client);
        views.add(view);
        return getGameInfo();
    }

    public synchronized boolean selectGame(String nickname, int id) {
        VirtualView view = getView(nickname);
        if (view == null) return false;

        if (!controllers.containsKey(id)) return false;

        if (controllers.get(id).isStarted()) return false;

        controllers.get(id).update(new JoinAction(nickname, view));
        boolean error = view.isError();

        if (!error) {
            view.setController(controllers.get(id));
        }

        return !view.isError();
    }

    public synchronized boolean createGame(String nickname, int numberPlayers, int numberCommonGoals) {
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2)
            return false;

        VirtualView view = getView(nickname);
        if (view == null) return false;

        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.update(new JoinAction(nickname, view));

        if (view.isError()) return false;

        view.setController(controller);
        addController(controller);
        return true;
    }

    public synchronized List<Item> selectFromLivingRoom(String nickname, List<Position> positions) {
        VirtualView view = getView(nickname);
        if (view == null) return null;

        Controller controller = view.getController();
        if (controller == null) return null;

        controller.update(new SelectionFromLivingRoomAction(nickname, positions));

        if (view.isError()) return null;

        return view.getItemsChosenRep().getItemsChosen();
    }

    public synchronized boolean putInBookshelf(String nickname, int column, List<Integer> permutation) {
        VirtualView view = getView(nickname);
        if (view == null) return false;

        Controller controller = view.getController();
        if (controller == null) return false;

        controller.update(new SelectionColumnAndOrderAction(nickname, column, permutation));

        return !view.isError();
    }

    public synchronized boolean addMessage(String nickname, String text) {
        VirtualView view = getView(nickname);
        if (view == null) return false;

        Controller controller = view.getController();
        if (controller == null) return false;

        controller.update(new ChatMessageAction(nickname, text));

        return !view.isError();
    }
}
