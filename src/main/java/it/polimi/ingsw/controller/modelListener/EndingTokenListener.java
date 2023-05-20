package it.polimi.ingsw.controller.modelListener;

public class EndingTokenListener extends ModelListener {
    private String owner;

    public EndingTokenListener() {
        super();
    }

    public String getEndingToken() {
        changed = false;
        String ret = owner;
        owner = null;

        return ret;
    }

    public void updateState(String owner) {
        changed = true;
        this.owner = owner;
    }
}
