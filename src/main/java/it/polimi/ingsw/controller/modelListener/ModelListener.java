package it.polimi.ingsw.controller.modelListener;

public abstract class ModelListener {
    protected boolean changed;

    public ModelListener() {
        changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }
}
