package it.polimi.ingsw.utils.networkMessage.server;

import it.polimi.ingsw.utils.Rank;
import it.polimi.ingsw.utils.networkMessage.NetworkMessage;

import java.util.List;

public class ScoresUpdate extends NetworkMessage {
    private final List<Rank> scores;

    public ScoresUpdate(List<Rank> scores) {
        this.scores = scores;
    }

    public List<Rank> getScores() {
        return scores;
    }
}
