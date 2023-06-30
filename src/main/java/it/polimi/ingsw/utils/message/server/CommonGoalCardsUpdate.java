package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Message sent by the server when updates to the {@code CommonGoalCard}s happen.
 */
public class CommonGoalCardsUpdate extends Message {
    private final Map<Integer, Integer> cards;

    /**
     * Constructor of the class. It sets the changes to the {@code CommonGoalCard}s stacks.
     * @param cards The {@code Map} of changes to the {@code CommonGoalCard}s: keys are IDs and values are
     *              the corresponding tops of the stacks.
     */
    public CommonGoalCardsUpdate(Map<Integer, Integer> cards) {
        this.cards = cards;
    }

    /**
     * Getter for the updates to the {@code CommonGoalCard}s.
     * @return The {@code CommonGoalCards}s updated
     */
    public Map<Integer, Integer> getCommonGoalCardsUpdate() {
        return new HashMap<>(cards);
    }
}
