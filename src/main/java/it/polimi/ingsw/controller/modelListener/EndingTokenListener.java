package it.polimi.ingsw.controller.modelListener;

public class EndingTokenListener extends ModelListener {
    private String owner;

    /**
     * Constructor for the class
     */
    public EndingTokenListener() {
        super();
    }

    /**
     * Getter for the owner of the ending token
     * @return - the owner
     */
    public String getEndingToken() {
        changed = false;
        String ret = owner;
        owner = null;

        return ret;
    }

    /**
     * This method changes the owner of the ending token
     * @param owner - owner of the ending token.
     */
    public void updateState(String owner) {
        changed = true;
        this.owner = owner;
    }
}
