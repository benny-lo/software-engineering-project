package it.polimi.ingsw.view.change;

import java.util.HashMap;
import java.util.Map;

public class CommonGoalCardsListener extends ModelListener {
    private final Map<Integer, Integer> cards;

    public CommonGoalCardsListener() {
        this.cards = new HashMap<>();
    }

    public Map<Integer, Integer> getCards() {
        changed = false;
        Map<Integer, Integer> ret = new HashMap<>(cards);

        cards.clear();
        return ret;
    }

    public void updateState(int id, int topStack) {
        changed = true;
        cards.put(id, topStack);
    }
}
