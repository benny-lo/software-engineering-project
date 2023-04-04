package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.ScoringToken;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonGoalCardManager {
    /**
     * List with all the cards.
     */
    private final List<CommonGoalCard> cards;

    /**
     * Class constructor: it initializes the common goal cards of the game.
     * @param numCommonGoalCards number of common goal cards in the game. (1/2)
     * @param numPlayers number of players in the game.
     */
    public CommonGoalCardManager(int numCommonGoalCards, int numPlayers) {
        List<CommonGoalPatternInterface> commonGoals = randomCommonGoals(numCommonGoalCards);
        cards = new ArrayList<>();
        for(int i = 0; i < commonGoals.size(); i++) {
            cards.add(new CommonGoalCard(i, numPlayers, commonGoals.get(i)));
        }
    }

    /**
     * Choose the common goals of the game.
     * @param numCommonGoalCards the number of common goal cards.
     * @return list containing all common goals of the game.
     */
    private List<CommonGoalPatternInterface> randomCommonGoals(int numCommonGoalCards) {
        List<CommonGoalPatternInterface> patterns = new ArrayList<>();
        patterns.add(new CommonGoalPatternCountGroups(4, 4, (s) -> {
            if (s.size() != 4) return false;
            List<Position> l = s.stream().sorted((p1, p2) -> (p1.getRow() != p2.getRow()) ? p1.getRow() - p2.getRow() : p1.getColumn() - p2.getColumn()).toList();
            Position firstPos = l.get(0);
            if (l.get(1).getRow() != firstPos.getRow() || l.get(1).getColumn() != firstPos.getColumn() + 1) return false;
            if (l.get(2).getRow() != firstPos.getRow() + 1 || l.get(2).getColumn() != firstPos.getColumn()) return false;
            if (l.get(3).getRow() != firstPos.getRow() + 1 || l.get(3).getColumn() != firstPos.getColumn() + 1) return false;
            return true;
        }));
        patterns.add(new CommonGoalPatternDistinctItems(2, false, 6, 6));
        patterns.add(new CommonGoalPatternCountGroups(4, 4, (s) -> s.size() == 4));
        patterns.add(new CommonGoalPatternCountGroups(2, 6, (s) -> s.size() == 2));
        patterns.add(new CommonGoalPatternDistinctItems(3, false, 1, 3));
        patterns.add(new CommonGoalPatternDistinctItems(2, true, 5, 5));
        patterns.add(new CommonGoalPatternDistinctItems(4, true, 1, 3));
        patterns.add(new CommonGoalPattern8());
        patterns.add(new CommonGoalPattern9());
        patterns.add(new CommonGoalPattern10());
        patterns.add(new CommonGoalPattern11());
        patterns.add(new CommonGoalPattern12());

        Collections.shuffle(patterns);

        return patterns.stream().limit(numCommonGoalCards).toList();
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
            if (cards.get(i).checkPattern(bookshelf)) tokens.add(cards.get(i).popToken());
        }
        return tokens;
    }
}
