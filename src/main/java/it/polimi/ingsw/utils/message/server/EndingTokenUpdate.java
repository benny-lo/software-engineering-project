package it.polimi.ingsw.utils.message.server;

import it.polimi.ingsw.utils.message.Message;

/**
 * Message sent by the server when somebody takes the ending token in a {@code Game}.
 */
public class EndingTokenUpdate extends Message {
    private final String owner;

    /**
     * Constructor for the class. It sets the owner of the ending token, {@code null} to represent no owner.
     * @param owner The owner of the ending token.
     */
    public EndingTokenUpdate(String owner) {
        this.owner = owner;
    }

    /**
     * Getter for the owner of the ending token.
     * @return The owner of the ending token.
     */
    public String getOwner() {
        return owner;
    }
}
