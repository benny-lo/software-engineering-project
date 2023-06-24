package it.polimi.ingsw.controller.modelListener;

public abstract class ModelListener {
    protected boolean changed;

    /**
     * Constructor for {@code this} class.
     */
    public ModelListener() {
        changed = false;
    }

    /**
     * Getter for the changed value
     * @return The changed value.
     */
    public boolean hasChanged() {
        return changed;
    }
}
