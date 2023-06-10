package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.*;

// TODO: propagate exceptions in Model and (add methods get dimensions --> where?).

// TODO: remove VirtualViews from lobby and controller with ServerUpdateView ?
// TODO: idea = make both lobby and game action listeners.

// TODO: set better timers.

/**
 * This class contains all {@code Controller}s of all games currently being played. It uses the Singleton
 * Pattern. All public methods take the lock on {@code this}.
 */
public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * Set of virtual views, representing all players connected either waiting for a game or in game.
     */
    private final Set<ServerUpdateViewInterface> views;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    private static Lobby instance;
    private final static Object lock = new Object();

    /**
     * The constructor for the class.
     */
    private Lobby() {
        controllers = new HashMap<>();
        views = new HashSet<>();
        availableId = 0;
    }

    /**
     * This method returns an Instance of Lobby, or creates a new one
     * @return - the instance of the lobby, either already existing or newly created.
     */
    public static Lobby getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new Lobby();
        }
        return instance;
    }

    /**
     * This method returns a list of game information for every game currently running, but not if they have already started.
     * @return - the list containing every game's info.
     */
    private List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            if (!controllers.get(id).isStarted())   //If a game has already started, it's not displayed.
                ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    /**
     * This method adds a new controller.
     * @param controller - the controller to be added.
     */
    public void addController(Controller controller) {
        controllers.put(availableId, controller);
        availableId++;
    }

    /**
     * This method removes a controller
     * @param controller - the controller that needs to be removed.
     */
    public synchronized void removeController(Controller controller) {
        Integer id = controllers.entrySet().stream().filter(entry -> controller.equals(entry.getValue())).findFirst().map(Map.Entry::getKey).orElse(-1);

        if (id != -1) controllers.remove(id);

        // notify not playing views that id is no longer available.
        (new Thread(() -> {
            List<GameInfo> list = new ArrayList<>();
            list.add(new GameInfo(id, -1, -1));
            for (ServerUpdateViewInterface v : views) {
                if (v.isLoggedIn() && !v.isInGame()) v.onGamesList(new GamesList(list));
            }
        })).start();
    }

    /**
     * This method adds a virtual view
     * @param view - the view to be added
     */
    public synchronized void addVirtualView(ServerUpdateViewInterface view) {
        views.add(view);
    }

    /**
     * This method removes a virtual view
     * @param view - the view to be removed
     */
    public synchronized void removeVirtualView(ServerUpdateViewInterface view) {
        views.remove(view);
    }

    /**
     * This method performs the login
     * @param nickname - the nickname of the player
     * @param view - the client's view
     */
    public synchronized void login(String nickname, ServerUpdateViewInterface view) {
        if(view.isLoggedIn() || view.isInGame()) {
            // the nickname is already chosen.
            view.onGamesList(new GamesList(null));
            return;
        }

        for(ServerUpdateViewInterface v : views) {
            if (nickname.equals(v.getNickname())) {
                view.onGamesList(new GamesList(null));
                return;
            }
        }

        System.out.println(nickname + " is connected");

        views.add(view);
        view.setNickname(nickname);
        view.onGamesList(new GamesList(getGameInfo()));
    }

    /**
     * This method creates a new game
     * @param numberPlayers - the number of players
     * @param numberCommonGoals - the number of common goal cards
     * @param view - the client's view
     */
    public synchronized void createGame(int numberPlayers, int numberCommonGoals, ServerUpdateViewInterface view) {
        // not yet registered.
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // incorrect parameters.
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // create controller + join operation in controller + set controller in view.
        Controller controller = new Controller(numberPlayers, numberCommonGoals);
        controller.perform(new JoinAction(view));
        addController(controller);
        // the controller is sending the player the game dimensions.

        view.setController(controller);

        // send to client without game
        (new Thread(() -> {
            for (ServerUpdateViewInterface v : views) {
                List<GameInfo> list = new ArrayList<>();
                list.add(new GameInfo(availableId - 1, numberPlayers, numberCommonGoals));
                GamesList gamesList = new GamesList(list);
                if (v.isLoggedIn() && !v.isInGame()) {
                    v.onGamesList(gamesList);
                }
            }
        })).start();
    }

    /**
     * This method allows the player to select a game
     * @param id - the id of the selected game
     * @param view - the view of the game
     */
    public synchronized void selectGame(int id, ServerUpdateViewInterface view) {
        if (!view.isLoggedIn() || view.isInGame()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
        }

        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        view.setController(controllers.get(id));

        controllers.get(id).perform(new JoinAction(view));
        // the controller is sending the player the game dimensions.
    }

    //FOR TESTING ONLY

    public Map<Integer, Controller> getControllers()
    {
        return this.controllers;
    }

    public Set<ServerUpdateViewInterface> getViews()
    {
        return this.views;
    }

    public int getAvailableId()
    {
        return this.availableId;
    }
}
