package it.polimi.ingsw.model.commonGoalCard.commonGoalPattern;

import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.model.player.Bookshelf;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Class representing patterns 1, 3 and 4. The general pattern is the following:
 * at least {@code matches} disjoint groups of adjacent tiles of the same kind satisfying {@code goodTile}.
 */
public class CommonGoalPatternCountGroups implements CommonGoalPatternInterface {
    private final int maxDimension;
    private final int matches;
    private int matchesFound;
    private final Predicate<Set<Position>> goodTile;

    /**
     * Class constructor: it initializes the private fields {@code matches} and {@code goodTile} of {@code this}.
     * @param matches the minimum number of matches required.
     * @param goodTile the predicate defining what groups to consider.
     * @param maxDimension the maximum dimension of group of tiles.
     */
    public CommonGoalPatternCountGroups(int maxDimension, int matches, Predicate<Set<Position>> goodTile) {
        this.maxDimension = maxDimension;
        this.matches = matches;
        this.matchesFound = 0;
        this.goodTile = goodTile;
    }

    /**
     * Check pattern on {@code Bookshelf} object.
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return - true if the pattern is found, false if it isn't
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        List<Set<Position>> allGroups = generateGroups(bookshelf);
        generateAllSubsets(allGroups, new ArrayList<>(), 0);

        return matchesFound >= matches;
    }

    /**
     * Method to generate all good groups of adjacent tiles.
     * @param bookshelf the bookshelf.
     * @return list of all good groups found in bookshelf.
     */
    private List<Set<Position>> generateGroups(Bookshelf bookshelf) {
        Set<Set<Position>> allGroups = new HashSet<>();
        Set<Position> curr = new HashSet<>();

        for(int i = 0; i < bookshelf.getRows(); i++) {
            for(int j = 0; j < bookshelf.getColumns(); j++) {
                if (bookshelf.tileAt(i, j) == null) continue;
                curr.clear();
                curr.add(new Position(i, j));
                helper(allGroups, bookshelf, curr);
            }
        }
        return new ArrayList<>(allGroups);
    }

    /**
     * helper method for {@code generateGroups}. It is used to generate all groups starting from a single position.
     * @param allGroups the set representing all groups currently found. It is augmented every time a new groups is found.
     * @param bookshelf the bookshelf.
     * @param curr the current set of positions being considered.
     */
    private void helper(Set<Set<Position>> allGroups, Bookshelf bookshelf, Set<Position> curr) {
        if (goodTile.test(curr)) {
            Set<Position> group = new HashSet<>();
            for(Position p : curr) group.add(new Position(p.getRow(), p.getColumn()));
            allGroups.add(group);
            return;
        }
        if (curr.size() > maxDimension) return;

        Set<Position> newCurr = new HashSet<>(curr);

        for(Position p : curr) {
            Position nextPosition = new Position(p.getRow()-1, p.getColumn());
            if (p.getRow() > 0 && bookshelf.tileAt(nextPosition) == bookshelf.tileAt(p) && !curr.contains(nextPosition)) {
                newCurr.add(nextPosition);
                helper(allGroups, bookshelf, newCurr);
                newCurr.remove(nextPosition);
            }

            nextPosition = new Position(p.getRow(), p.getColumn()-1);
            if (p.getColumn() > 0 && bookshelf.tileAt(nextPosition) == bookshelf.tileAt(p) && !curr.contains(nextPosition)) {
                newCurr.add(nextPosition);
                helper(allGroups, bookshelf, newCurr);
                newCurr.remove(nextPosition);
            }
        }
    }

    /**
     * Method generating all possible non-overlapping coverings of the bookshelf and finding the size of the largest.
     * @param groups all valid groups of tiles.
     * @param curr the current subset considered.
     * @param idx the index to decide whether to insert or exclude.
     */
    void generateAllSubsets(List<Set<Position>> groups, List<Set<Position>> curr, int idx) {
        if (!nonOverlapping(curr)) return;
        matchesFound = Math.max(curr.size(), matchesFound);
        if (idx == groups.size()) return;

        curr.add(groups.get(idx));
        generateAllSubsets(groups, curr, idx+1);

        curr.remove(curr.size() - 1);
        generateAllSubsets(groups, curr, idx+1);
    }

    /**
     * Method testing whether two groups of tiles share a position.
     * @param group1 the first group.
     * @param group2 the second group.
     * @return {@code true} iff the two groups share a position, i.e. they overlap.
     */
    private boolean nonOverlapping(Set<Position> group1, Set<Position> group2) {
        for(Position p : group1) {
            if (group2.contains(p)) return false;
        }
        return true;
    }

    /**
     * Method testing whether a list of groups forms a valid non-overlapping covering.
     * @param groupTiles the candidate covering.
     * @return {@code true} iff the covering is valid.
     */
    private boolean nonOverlapping(List<Set<Position>> groupTiles) {
        for(Set<Position> group1 : groupTiles) {
            for(Set<Position> group2 : groupTiles) {
                if (group1.equals(group2)) continue;
                if (!nonOverlapping(group1, group2)) return false;
            }
        }
        return true;
    }
}
