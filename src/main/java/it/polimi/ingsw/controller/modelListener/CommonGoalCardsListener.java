package it.polimi.ingsw.controller.modelListener;

import java.util.HashMap;
import java.util.Map;

public class CommonGoalCardsListener extends ModelListener {
    private final Map<Integer, Integer> cards;

    /**
     * Constructor for the class
     */
    public CommonGoalCardsListener() {
        this.cards = new HashMap<>();
    }

    /**
     * Getter for the Common Goal Cards
     * @return - a map of the CommonGoalCards
     */
    public Map<Integer, Integer> getCards() {
        changed = false;
        Map<Integer, Integer> ret = new HashMap<>(cards);

        cards.clear();
        return ret;
    }

    /**
     * This method updates the Common Goal Cards
     * @param id - the id of the new card
     * @param topStack - the top of the stack
     */
    public void updateState(int id, int topStack) {
        changed = true;
        cards.put(id, topStack);
    }
}
