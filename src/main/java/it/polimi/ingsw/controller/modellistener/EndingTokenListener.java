package it.polimi.ingsw.controller.modellistener;

/**
 * Listener of the ending token: it registers when some player takes the ending token.
 */
public class EndingTokenListener extends ModelListener {
    private String owner;

    /**
     * Constructor for the class. It initializes {@code this} with an empty state.
     */
    public EndingTokenListener() {
        super();
    }

    /**
     * Getter for the current owner of the ending token. The state of {@code this} is NOT reset.
     * @return The nickname of the owner of the ending token.
     */
    public String getEndingToken() {
        changed = false;
        return owner;
    }

    /**
     * Registers a new owner of the ending token.
     * The state of {@code this} is set to non-empty.
     * @param owner The new owner of the ending token.
     */
    public void updateState(String owner) {
        changed = true;
        this.owner = owner;
    }
}
