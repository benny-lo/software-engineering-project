package it.polimi.ingsw.controller.modelListener;

import java.util.HashMap;
import java.util.Map;

public class CommonGoalCardsListener extends ModelListener {
    private final Map<Integer, Integer> cards;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     */
    public CommonGoalCardsListener() {
        this.cards = new HashMap<>();
    }

    /**
     * Getter for the latest updates to the {@code CommonGoalCard}s. The state of {@code this} is set to empty.
     * @return {@code Map} where keys are IDs of {@code CommonGoalCard}s and values are the corresponding top score.
     */
    public Map<Integer, Integer> getCards() {
        changed = false;
        Map<Integer, Integer> ret = new HashMap<>(cards);

        cards.clear();
        return ret;
    }

    /**
     * Registers a change in the {@code CommonGoalCard}s: the top of one of the stacks changed.
     * The state of {@code this} is set to non-empty.
     * @param id The ID of the {@code CommonGoalCard} whose top of stack changed.
     * @param topStack The new top of stack.
     */
    public void updateState(int id, int topStack) {
        changed = true;
        cards.put(id, topStack);
    }
}
