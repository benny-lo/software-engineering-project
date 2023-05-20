package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.message.server.AcceptedAction;
import it.polimi.ingsw.utils.message.server.AcceptedActionTypes;
import it.polimi.ingsw.utils.message.server.GameInfo;
import it.polimi.ingsw.utils.message.server.GamesList;
import it.polimi.ingsw.view.server.VirtualView;

import java.util.*;

// TODO: send scores in controller and manage disconnection and exceptions correctly.
// TODO: remove VirtualViews correctly so that the garbage collector can work its magic.
// TODO: add new messages + send client updates about games.

public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * Set of virtual views, representing all players connected either waiting for a game or in game.
     */
    private final Set<VirtualView> views;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    private static Lobby instance;
    private final static Object lock = new Object();

    private Lobby() {
        controllers = new HashMap<>();
        views = new HashSet<>();
        availableId = 0;
    }

    public static Lobby getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new Lobby();
        }
        return instance;
    }

    private List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            if (!controllers.get(id).isStarted())   //If a game has already started, it's not displayed.
                ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    private void addController(Controller controller) {
        controllers.put(availableId, controller);
        availableId++;
    }

    public synchronized void addVirtualView(VirtualView view) {
        views.add(view);
    }

    public synchronized void login(String nickname, VirtualView view) {
        if(view.isLoggedIn() || view.isInGame()) {
            // the nickname is already chosen.
            view.onGamesList(new GamesList(null));
            return;
        }

        views.add(view);
        view.setNickname(nickname);
        view.onGamesList(new GamesList(getGameInfo()));
    }

    public synchronized void createGame(int numberPlayers, int numberCommonGoals, VirtualView view) {
        // not yet registered.
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.CREATE_GAME));
            return;
        }

        // incorrect parameters.
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.onAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.CREATE_GAME));
            return;
        }

        // create controller + join operation in controller + set controller in view.
        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.update(new JoinAction(view.getNickname(), view));
        addController(controller);

        view.setController(controller);
    }


    public synchronized void selectGame(int id, VirtualView view) {
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.SELECT_GAME));
        }

        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.onAcceptedAction(new AcceptedAction(false, AcceptedActionTypes.SELECT_GAME));
            return;
        }

        controllers.get(id).update(new JoinAction(view.getNickname(), view));
    }
}
