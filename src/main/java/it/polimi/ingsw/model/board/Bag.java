package it.polimi.ingsw.model.board;

import it.polimi.ingsw.utils.Item;

import java.util.*;

/**
 * Class representing the Bag in the game. It contains {@code Item}s.
 */
public class Bag {
    /**
     * Stack containing all currently remaining {@code Item}s in {@code this}.
     */
    private final Deque<Item> elements;

    /**
     * Constructor for the class. Fills {@code this} and randomly shuffles it.
     * @param numberPerType Number of {@code Item}s of each type to put in {@code this}.
     */
    public Bag(int numberPerType) {
        List<Item> items = new ArrayList<>();
        for(Item item : Item.values()) {
            if (item == Item.LOCKED) continue;
            for (int i = 0; i < numberPerType; i++) items.add(item);
        }
        Collections.shuffle(items);

        this.elements = new ArrayDeque<>(items);
    }

    /**
     * Checks if {@code this} is empty.
     * @return {@code true} iff {@code this} is empty.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Extracts an {@code Item} from {@code this}.
     * @return The {@code Item} extracted if {@code this} was not empty, else null.
     */
    public Item extract() {
        try {
            return elements.pop();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
