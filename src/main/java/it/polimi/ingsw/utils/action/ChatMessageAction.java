package it.polimi.ingsw.utils.action;

public class ChatMessageAction extends Action {
    private final String text;

    public ChatMessageAction(String nickname, String text) {
        super(nickname);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
