package it.polimi.ingsw.controller.modelListener;

public abstract class ModelListener {
    protected boolean changed;

    /**
     * Constructor for the class.
     */
    public ModelListener() {
        changed = false;
    }

    /**
     * Getter for the changed value
     * @return - the changed value.
     */
    public boolean hasChanged() {
        return changed;
    }
}
