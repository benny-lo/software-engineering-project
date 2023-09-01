package it.polimi.ingsw.controller.modelListener;

import it.polimi.ingsw.utils.message.server.ChatUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Listener to the chat. The main purpose of this class is to store all {@code ChatUpdate}s, so that if a client reconnects
 * all lost messages can be sent to them.
 */
public class ChatListener extends ModelListener {
    private static final String BROADCAST = "all";
    private final Map<String, List<ChatUpdate>> updatesPerPlayer;

    /**
     * Constructs an instance of the class. It initializes the state as empty.
     */
    public ChatListener() {
        super();
        this.updatesPerPlayer = new HashMap<>();
        updatesPerPlayer.put(BROADCAST, new ArrayList<>());
    }

    /**
     * Adds to the state a {@code ChatUpdate} corresponding to a chat message.
     * @param update The update to add.
     */
    public void addUpdate(ChatUpdate update) {
        String sender = update.getSender();
        String receiver = update.getReceiver();

        if (BROADCAST.equals(sender) || BROADCAST.equals(receiver)) {
            updatesPerPlayer.get(BROADCAST).add(update);
            return;
        }

        if (!updatesPerPlayer.containsKey(sender)) updatesPerPlayer.put(sender, new ArrayList<>());
        if (!updatesPerPlayer.containsKey(receiver)) updatesPerPlayer.put(receiver, new ArrayList<>());
        updatesPerPlayer.get(sender).add(update);
        updatesPerPlayer.get(receiver).add(update);
    }

    /**
     * Getter for the {@code ChatUpdate}s related to a given player.
     * @param nickname The nickname of the player.
     * @return {@code List} containing all {@code ChatUpdate}s related to the player with {@code nickname}.
     */
    public List<ChatUpdate> getChatUpdates(String nickname) {
        if (!updatesPerPlayer.containsKey(nickname)) return new ArrayList<>();

        List<ChatUpdate> arr = new ArrayList<>(updatesPerPlayer.get(nickname));
        arr.addAll(updatesPerPlayer.get(BROADCAST));
        return arr;
    }
}
