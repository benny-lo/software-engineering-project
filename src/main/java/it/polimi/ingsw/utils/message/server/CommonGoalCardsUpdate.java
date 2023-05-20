package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

import java.util.Map;

public class CommonGoalCardsUpdate extends Message {
    private final Map<Integer, Integer> cards;

    public CommonGoalCardsUpdate(Map<Integer, Integer> cards) {
        this.cards = cards;
    }

    public Map<Integer, Integer> getCommonGoalCardsUpdate() {
        return cards;
    }
}
