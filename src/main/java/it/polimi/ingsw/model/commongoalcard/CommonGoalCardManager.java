package it.polimi.ingsw.model.commongoalcard;

import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.commongoalcard.pattern.*;
import it.polimi.ingsw.model.player.Bookshelf;
import it.polimi.ingsw.controller.modellistener.CommonGoalCardsListener;

import java.util.*;

/**
 * Class managing all {@code CommonGoalCard}s of a game.
 */
public class CommonGoalCardManager {
    /**
     * List with all the cards.
     */
    private final List<CommonGoalCard> cards;
    private CommonGoalCardsListener commonGoalCardsListener;

    /**
     * Constructor for the class. It initializes the {@code CommonGoalCard}s of the game.
     * @param numCommonGoalCards Number of common goal cards in the game. (1/2)
     * @param numPlayers Number of players in the game.
     */
    public CommonGoalCardManager(int numCommonGoalCards, int numPlayers) {
        List<CommonGoalPatternInterface> commonGoals = commonGoals();
        cards = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < 12; i++) ids.add(i);
        Collections.shuffle(ids);

        for(int i = 0; i < numCommonGoalCards; i++) {
            int id = ids.get(i);
            cards.add(new CommonGoalCard(id, numPlayers, commonGoals.get(id)));
        }
    }

    /**
     * Constructor of the class exclusively used for testing: it initializes the common goal cards of the game.
     * @param numPlayers Number of players in the game.
     */
    public CommonGoalCardManager(int numPlayers) {
        cards = new ArrayList<>();
        cards.add(new CommonGoalCard(7, numPlayers, new CommonGoalPattern8()));
        cards.add(new CommonGoalCard(8, numPlayers, new CommonGoalPattern9()));
    }

    /**
     * Generates all possible {@code CommonGoalPatternInterface}s.
     * @return {@code List} containing all possible {@code CommonGoalPatternInterface}s.
     */
    private List<CommonGoalPatternInterface> commonGoals() {
        List<CommonGoalPatternInterface> pattern = new ArrayList<>();
        for(int i = 0; i < 12; i++) {
            if (i == 0)
                pattern.add(new CommonGoalPatternCountGroups(4, 2, s -> {
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
                pattern.add(new CommonGoalPatternCountGroups(4, 4, s -> s.size() == 4));
            if (i == 3)
                pattern.add(new CommonGoalPatternCountGroups(2, 6, s -> s.size() == 2));
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
     * Checks all not yet achieved common goals and gets the {@code ScoringToken}s.
     * @param bookshelf The {@code Bookshelf} to check the patterns on.
     * @param cannotTake {@code List} of IDs of {@code CommonGoalCard}s not to consider.
     * @return The scoring token from the common goal cards achieved.
     */
    public List<ScoringToken> check(Bookshelf bookshelf, List<Integer> cannotTake) {
        List<ScoringToken> tokens = new ArrayList<>();
        for(CommonGoalCard card : cards) {
            if (cannotTake.contains(card.getId())) continue;
            if (card.checkPattern(bookshelf)) {
                ScoringToken token = card.popToken();
                if (token != null) tokens.add(token);
                if (commonGoalCardsListener != null) {
                    commonGoalCardsListener.updateState(card.getId(), card.getTopStack());
                }
            }
        }
        return tokens;
    }

    /**
     * Sets the given CommonGoalCardsListener and updates with the current state of the {@code CommonGoalCard}s.
     * @param commonGoalCardsListener {@code CommonGoalCardListener} to set.
     */
    public void setCommonGoalCardsListener(CommonGoalCardsListener commonGoalCardsListener) {
        this.commonGoalCardsListener = commonGoalCardsListener;
        for(CommonGoalCard card : cards) {
            commonGoalCardsListener.updateState(card.getId(), card.getTopStack());
        }
    }
}
