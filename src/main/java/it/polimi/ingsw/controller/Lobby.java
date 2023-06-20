package it.polimi.ingsw.controller;

import it.polimi.ingsw.utils.Logger;
import it.polimi.ingsw.utils.action.JoinAction;
import it.polimi.ingsw.utils.message.server.*;
import it.polimi.ingsw.view.server.ServerUpdateViewInterface;

import java.util.*;


/**
 * Class containing all {@code Controller}s of all games currently being played. It uses the Singleton
 * Pattern. Some public methods take the lock on {@code this}.
 */
public class Lobby {
    /**
     * Map of id -> games (the controller of that game).
     */
    private final Map<Integer, Controller> controllers;

    /**
     * Set of all logged in (with a nickname) {@code ServerUpdateViewInterface}s,
     * representing all players connected either waiting for a game or in game.
     * They can be used by {@code this} to send updates to a player.
     */
    private final Set<ServerUpdateViewInterface> views;

    /**
     * Smallest available id for the next game to be created.
     */
    private int availableId;

    /**
     * Attribute used to implement the Singleton Pattern.
     */
    private static Lobby instance;

    /**
     * Object only used as a lock by the {@code getInstance} method for synchronization.
     */
    private final static Object lock = new Object();

    /**
     * The constructor for the class. Initializes the lobby with an empty map of {@code Controller}s
     * and an empty set of {@code ServerUpdateViewInterface}s. The first available id is 0.
     */
    private Lobby() {
        controllers = new HashMap<>();
        views = new HashSet<>();
        availableId = 0;
    }

    /**
     * Getter for the instance of {@code Lobby}. If no instance exists, it creates a new one.
     * @return the instance of the lobby, either already existing or newly created.
     */
    public static Lobby getInstance() {
        synchronized (lock) {
            if (instance == null) instance = new Lobby();
        }
        return instance;
    }

    /**
     * Getter for the list of games information for every match in pre-game phase.
     * @return a list containing the game's info.
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
     * Adds a new controller to {@code Lobby}.
     * @param controller the controller to add.
     */
    private void addController(Controller controller) {
        controllers.put(availableId, controller);
        availableId++;
    }

    /**
     * Removes a controller from {@code Lobby}. It synchronizes on {@code this}. Moreover, it notifies
     * asynchronously all players still waiting to join a match.
     * @param controller the controller to remove.
     */
    public synchronized void removeController(Controller controller) {
        Integer id = controllers.entrySet().stream().filter(entry -> controller.equals(entry.getValue())).findFirst().map(Map.Entry::getKey).orElse(-1);

        if (id != -1) controllers.remove(id);
    }

    /**
     * Unsubscribes a {@code ServerUpdateViewInterface} from updates sent by {@code this}.
     * It synchronizes on {@code this}.
     * @param view - the view to be removed
     */
    public synchronized void removeServerUpdateViewInterface(ServerUpdateViewInterface view) {
        views.remove(view);
    }

    /**
     * Processes a login request from a {@code ServerUpdateViewInterface} and registers that view.
     * It synchronizes on {@code this}.
     * @param nickname the nickname chosen by the {@code ServerUpdateViewInterface}.
     * @param view the {@code ServerUpdateViewInterface} performing the request.
     */
    public synchronized void login(String nickname, ServerUpdateViewInterface view) {
        if(views.contains(view)) {
            // this view has already logged in.
            view.onGamesList(new GamesList(null));
            return;
        }

        for(ServerUpdateViewInterface v : views) {
            if (nickname.equals(v.getNickname())) {
                // a view with nickname already exists.
                view.onGamesList(new GamesList(null));
                return;
            }
        }

        synchronized (System.out) {
            Logger.login(nickname);
        }

        views.add(view);
        view.setNickname(nickname);
        view.onGamesList(new GamesList(getGameInfo()));
    }

    /**
     * Creates a new {@code Controller} with the parameters requested and logs the
     * {@code ServerUpdateViewInterface} that performed the request into the newly created game.
     * Moreover, all clients that have not joined in a match yet are notified of the creation of the
     * match.
     * It synchronizes on {@code this}.
     * @param numberPlayers the number of players in the game to create.
     * @param numberCommonGoals the number of common goal cards in the game to create.
     * @param view the {@code ServerUpdateViewInterface} that performed the request.
     */
    public synchronized void createGame(int numberPlayers, int numberCommonGoals, ServerUpdateViewInterface view) {
        // not yet registered or already playing.
        if (!views.contains(view) || view.isInGame()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // incorrect parameters.
        if (numberPlayers < 2 || numberPlayers > 4 || numberCommonGoals < 1 || numberCommonGoals > 2) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // create controller + join operation in controller
        Controller controller = new Controller(numberPlayers, numberCommonGoals, availableId);
        controller.perform(new JoinAction(view));

        // check if the JoinAction worked
        if (controller.getNumberActualPlayers() == 0) {
            return;
        }

        // set controller in view.
        addController(controller);

        Logger.createGame(numberPlayers, numberCommonGoals, availableId - 1, view.getNickname());

        // the controller is sending the player the game dimensions.
        view.setController(controller);

        // send to client without game
        (new Thread(() -> {
            synchronized (this) {
                for (ServerUpdateViewInterface v : views) {
                    List<GameInfo> list = new ArrayList<>();
                    list.add(new GameInfo(availableId - 1, numberPlayers, numberCommonGoals));
                    GamesList gamesList = new GamesList(list);
                    if (!v.isInGame()) {
                        v.onGamesList(gamesList);
                    }
                }
            }
        })).start();
    }

    /**
     * Joins the {@code ServerUpdateViewInterface} that performed the request in the selected math (by id).
     * @param id the id of the selected game.
     * @param view the {@code ServerUpdateViewInterface} that performed the request.
     */
    public synchronized void selectGame(int id, ServerUpdateViewInterface view) {
        // not yet registered or already in a match.
        if (!views.contains(view) || view.isInGame()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
        }

        // selected match does not exist or is already started.
        if (!controllers.containsKey(id) || controllers.get(id).isStarted()) {
            view.onGameData(new GameData(-1, null, -1, -1, -1, -1, -1));
            return;
        }

        // join successful.
        view.setController(controllers.get(id));

        Logger.selectGame(id, view.getNickname());

        controllers.get(id).perform(new JoinAction(view));
        // the controller is sending the player the game dimensions.
    }

    //FOR TESTING ONLY
    public static void setNull()
    {
        instance = null;
    }
}
