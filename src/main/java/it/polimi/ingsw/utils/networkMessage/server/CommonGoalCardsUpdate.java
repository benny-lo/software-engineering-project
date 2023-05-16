package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.Map;

public class CommonGoalCardsUpdate extends NetworkMessage {
    private final Map<Integer, Integer> cards;

    public CommonGoalCardsUpdate(Map<Integer, Integer> cards) {
        this.cards = cards;
    }

    public Map<Integer, Integer> getCommonGoalCardsUpdate() {
        return cards;
    }
}
