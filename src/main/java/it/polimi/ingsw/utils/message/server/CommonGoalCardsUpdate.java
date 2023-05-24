package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class CommonGoalCardsUpdate extends Message {
    private final Map<Integer, Integer> cards;

    /**
     * Constructor of the class
     * @param cards - the common goal cards that are now in play
     */
    public CommonGoalCardsUpdate(Map<Integer, Integer> cards) {
        this.cards = cards;
    }
    /**
     * Getter for the common goal cards
     * @return - the common goal cards updated
     */
    public Map<Integer, Integer> getCommonGoalCardsUpdate() {
        return cards;
    }
}
