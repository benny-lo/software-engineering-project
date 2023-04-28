package network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.utils.GameInfo;
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

    public List<GameInfo> getGameInfo() {
        List<GameInfo> ret = new ArrayList<>();
        for (Integer id : controllers.keySet()) {
            ret.add(new GameInfo(id, controllers.get(id).getNumberPlayers(), controllers.get(id).getNumberCommonGoalCards()));
        }
        return ret;
    }

    public void addController(Controller controller){
        controllers.put(availableId, controller);
        availableId++;
    }

    public void addVirtualView(VirtualView view) {
        views.add(view);
    }

    public Map<Integer, Controller> getControllers() {
        return controllers;
    }

    public VirtualView getView(String nickname) {
        for(VirtualView view : views) {
            if (view.getNickname().equals(nickname)) return view;
        }
        return null;
    }
}
