package it.polimi.ingsw.utils.action;

/**
 * The listener for players' actions.
 */
public interface ActionListener {
    void update(JoinAction action);
    void update(SelectionFromLivingRoomAction action);
    void update(SelectionColumnAndOrderAction action);
    void update(ChatMessageAction action);
}
