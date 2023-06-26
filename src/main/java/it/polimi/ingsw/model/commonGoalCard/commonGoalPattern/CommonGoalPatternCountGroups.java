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
     * Constructor for the class. It sets the {@code matches}, {@code goodTile} and {@code maxDimension} of {@code this}.
     * @param maxDimension The maximum dimension of group of {@code Position}s.
     * @param matches The minimum number of matches required.
     * @param goodTile The {@code Predicate} defining what groups to consider.
     */
    public CommonGoalPatternCountGroups(int maxDimension, int matches, Predicate<Set<Position>> goodTile) {
        this.maxDimension = maxDimension;
        this.matches = matches;
        this.matchesFound = 0;
        this.goodTile = goodTile;
    }

    /**
     * {@inheritDoc}
     * @param bookshelf {@code Bookshelf} object to check the pattern on.
     * @return {@code true} iff the pattern is present in {@code bookshelf}.
     */
    @Override
    public boolean check(Bookshelf bookshelf) {
        List<Set<Position>> allGroups = generateGroups(bookshelf);
        generateAllSubsets(allGroups, new ArrayList<>(), 0);

        return matchesFound >= matches;
    }

    /**
     * Generates all good groups of adjacent tiles from a {@code Bookshelf}.
     * @param bookshelf The {@code Bookshelf}.
     * @return {@code List} of all good groups found in {@code bookshelf}.
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
     * Recursive helper method for {@code generateGroups}. It is used to generate all groups starting from a single position.
     * @param allGroups The set representing all groups currently found. It is augmented every time a new groups is found.
     * @param bookshelf The bookshelf.
     * @param curr The current set of positions being considered.
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
     * Generates all possible non-overlapping coverings of the bookshelf and finds the size of the largest.
     * @param groups All good groups of tiles.
     * @param curr The current subset considered.
     * @param idx The index to decide whether to insert or exclude.
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
     * Checks whether two groups of tiles share a {@code Position}.
     * @param group1 The first group.
     * @param group2 The second group.
     * @return {@code true} iff the two groups share a position, i.e. they overlap.
     */
    private boolean nonOverlapping(Set<Position> group1, Set<Position> group2) {
        for(Position p : group1) {
            if (group2.contains(p)) return false;
        }
        return true;
    }

    /**
     * Checks whether a {@code List} of groups forms a valid non-overlapping covering.
     * @param groupTiles The candidate covering.
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
