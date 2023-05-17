package it.polimi.ingsw.model.commonGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commonGoalCard.commonGoalPattern.*;
import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.view.modelListener.CommonGoalCardsListener;

import java.util.*;

public class CommonGoalCardManager {
    /**
     * List with all the cards.
     */
    private final List<CommonGoalCard> cards;
    private final List<CommonGoalCardsListener> commonGoalCardsReps;

    /**
     * Class constructor: it initializes the common goal cards of the game.
     * @param numCommonGoalCards number of common goal cards in the game. (1/2)
     * @param numPlayers number of players in the game.
     */
    public CommonGoalCardManager(int numCommonGoalCards, int numPlayers) {
        List<CommonGoalPatternInterface> commonGoals = commonGoals();
        cards = new ArrayList<>();
        commonGoalCardsReps = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < 12; i++) ids.add(i);
        Collections.shuffle(ids);

        for(int i = 0; i < numCommonGoalCards; i++) {
            int id = ids.get(i);
            cards.add(new CommonGoalCard(id, numPlayers, commonGoals.get(id)));
        }
    }

    /**
     * Class constructor exclusively used for testing: it initializes the common goal cards of the game.
     * @param numPlayers number of players in the game.
     * @param test Test.
     */
    public CommonGoalCardManager(int numPlayers, String test) {
        cards = new ArrayList<>();
        commonGoalCardsReps = new ArrayList<>();
        cards.add(new CommonGoalCard(0, numPlayers, new CommonGoalPattern8()));
        cards.add(new CommonGoalCard(1, numPlayers, new CommonGoalPattern9()));
    }

    /**
     * All possible common goals.
     * @return list containing all common goals of the game.
     */
    private List<CommonGoalPatternInterface> commonGoals() {
        List<CommonGoalPatternInterface> pattern = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            if (i == 0)
                pattern.add(new CommonGoalPatternCountGroups(4, 4, (s) -> {
                    if (s.size() != 4) return false;
                    List<Position> l = s.stream().sorted((p1, p2) -> (p1.getRow() != p2.getRow()) ? p1.getRow() - p2.getRow() : p1.getColumn() - p2.getColumn()).toList();
                    Position firstPos = l.get(0);
                    if (l.get(1).getRow() != firstPos.getRow() || l.get(1).getColumn() != firstPos.getColumn() + 1)
                        return false;
                    if (l.get(2).getRow() != firstPos.getRow() + 1 || l.get(2).getColumn() != firstPos.getColumn())
                        return false;
                    return l.get(3).getRow() == firstPos.getRow() + 1 && l.get(3).getColumn() == firstPos.getColumn() + 1;
                }));
            if (i == 1)
                pattern.add(new CommonGoalPatternDistinctItems(2, false, 6, 6));
            if (i == 2)
                pattern.add(new CommonGoalPatternCountGroups(4, 4, (s) -> s.size() == 4));
            if (i == 3)
                pattern.add(new CommonGoalPatternCountGroups(2, 6, (s) -> s.size() == 2));
            if (i == 4)
                pattern.add(new CommonGoalPatternDistinctItems(3, false, 1, 3));
            if (i == 5)
                pattern.add(new CommonGoalPatternDistinctItems(2, true, 5, 5));
            if (i == 6)
                pattern.add(new CommonGoalPatternDistinctItems(4, true, 1, 3));
            if (i == 7)
                pattern.add(new CommonGoalPattern8());
            if (i == 8)
                pattern.add(new CommonGoalPattern9());
            if (i == 9)
                pattern.add(new CommonGoalPattern10());
            if (i == 10)
                pattern.add(new CommonGoalPattern11());
            if (i == 11)
                pattern.add(new CommonGoalPattern12());
        }

        return pattern;
    }

    /**
     * Perform check on all not yet achieved common goal cards and gets the scoring tokens.
     * @param bookshelf the bookshelf.
     * @param cannotTake list of indices of common goal cards not to consider.
     * @return the scoring token from the common goal cards achieved.
     */
    public List<ScoringToken> check(Bookshelf bookshelf, List<Integer> cannotTake) {
        List<ScoringToken> tokens = new ArrayList<>();
        for(int i = 0; i < cards.size(); i++) {
            if (cannotTake.contains(i)) continue;
            if (cards.get(i).checkPattern(bookshelf)) {
                tokens.add(cards.get(i).popToken());
                for(CommonGoalCardsListener rep : commonGoalCardsReps) {
                    rep.updateState(cards.get(i).getId(), cards.get(i).getTopStack());
                }
            }
        }
        return tokens;
    }

    public void setCommonGoalCardsRep(CommonGoalCardsListener rep) {
        commonGoalCardsReps.add(rep);
        for(CommonGoalCard card : cards) {
            rep.updateState(card.getId(), card.getTopStack());
        }
    }
}
