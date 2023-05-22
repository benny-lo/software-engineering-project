package it.polimi.ingsw.utils.action;

/**
 * The listener for players' actions.
 */
public interface ActionListener {
    void perform(JoinAction action);
    void perform(SelectionFromLivingRoomAction action);
    void perform(SelectionColumnAndOrderAction action);
    void perform(ChatMessageAction action);
    void perform(DisconnectionAction action);
}
