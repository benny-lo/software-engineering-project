package it.polimi.ingsw.view.rep;

import java.util.ArrayList;
import java.util.List;

public class CommonGoalCardsRep extends Rep {
    private final List<Integer> ids;
    private final List<Integer> tops;

    public CommonGoalCardsRep() {
        this.ids = new ArrayList<>();
        this.tops = new ArrayList<>();
    }

    public List<Integer> getIDs() {
        peek();
        return ids;
    }

    public List<Integer> getTops() {
        peek();
        return tops;
    }

    public void updateRep(int id, int topStack) {
        update();
        if (!ids.contains(id)) ids.add(id);

        int idx = ids.indexOf(id);
        tops.set(idx, topStack);
    }
}
