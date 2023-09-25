package it.polimi.ingsw.controller.modellistener;

import java.util.HashMap;
import java.util.Map;

/**
 * Listener to the {@code CommonGoalCard}s: it registers the change in the top of the stack
 * of any {@code CommonGoalCard}.
 */
public class CommonGoalCardsListener extends ModelListener {
    /**
     * Map storing the most recent (not yet queried) updates to the tops of the stacks of the Common Goal Cards.
     */
    private final Map<Integer, Integer> cardsUpdates;

    /**
     * Map storing the top of the stack of every common goal card, used in case of disconnection.
     */
    private final Map<Integer, Integer> cardsState;


    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     */
    public CommonGoalCardsListener() {
        this.cardsUpdates = new HashMap<>();
        this.cardsState = new HashMap<>();
    }

    /**
     * Getter for the latest updates to the {@code CommonGoalCard}s. The state of {@code this} is set to empty.
     * @return {@code Map} where keys are IDs of {@code CommonGoalCard}s and values are the corresponding top score.
     */
    public Map<Integer, Integer> getCardsUpdates() {
        changed = false;
        Map<Integer, Integer> ret = new HashMap<>(cardsUpdates);
        cardsUpdates.clear();
        return ret;
    }

    /**
     * Getter for the full representation of the {@code CommonGoalCard}s.
     * @return {@code Map} representing the stack of every {@code CommonGoalCard}.
     */
    public Map<Integer, Integer> getCardsState() {
        return new HashMap<>(cardsState);
    }

    /**
     * Registers a change in the {@code CommonGoalCard}s: the top of one of the stacks changed.
     * The state of {@code this} is set to non-empty.
     * @param id The ID of the {@code CommonGoalCard} whose top of stack changed.
     * @param topStack The new top of stack.
     */
    public void updateState(int id, int topStack) {
        changed = true;
        cardsUpdates.put(id, topStack);
        cardsState.put(id, topStack);
    }
}
