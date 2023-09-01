package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.ChatUpdate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing Chat controller.
 * It manages the messages of the various players, that can be sent in broadcast or in unicast.
 */
public class ChatController extends AbstractController {
    private final static String BROADCAST = "all";
    @FXML
    private ComboBox<String> playerMenu;
    @FXML
    private VBox messagesDisplayed;
    @FXML
    private TextField textToSend;

    private final List<ChatUpdate> messages = new ArrayList<>();
    private final List<String> disconnectedPlayers = new ArrayList<>();

    /**
     * Sets {@code this} in the {@code GUInterface}.
     * Sets 'all' and the nicknames of the connected players to the {@code playerMenu}.
     */
    @FXML
    public void initialize() {
        guInterface.setController(this);
        playerMenu.getItems().add("all");
        playerMenu.setValue("all");

        List<String> nicknames = guInterface.getOthersNicknames();
        for(String nick : nicknames) {
            playerMenu.getItems().add(nick);
        }
    }

    /**
     * Adds the message received to the chat.
     * @param message The message received.
     */
    public void receiveMessage(ChatUpdate message) {
        messages.add(message);
        if (isToDisplay(message)) {
            messagesDisplayed.getChildren().add(createLabel(message));
        }
    }

    /**
     * Sends to the {@code GUInterface} the message written by the client.
     */
    public void onSendButtonClick() {
        String text = textToSend.getText();
        String receiver = playerMenu.getValue();
        ChatMessage message = new ChatMessage(text, receiver);

        textToSend.clear();

        guInterface.writeChat(message);
    }

    /**
     * Displays the messages between the client and the player selected or the 'all' chat.
     */
    public void onPlayerFromMenu() {
        messagesDisplayed.getChildren().clear();
        for (ChatUpdate message : messages) {
            if (isToDisplay(message)) messagesDisplayed.getChildren().add(createLabel(message));
        }
    }

    private boolean isToDisplay(ChatUpdate message) {
        if (isBroadcast(message)) return BROADCAST.equals(playerMenu.getValue());
        return playerMenu.getValue().equals(message.getSender()) ||
                playerMenu.getValue().equals(message.getReceiver());
    }

    private boolean isBroadcast(ChatUpdate message) {
        return BROADCAST.equals(message.getReceiver()) || BROADCAST.equals(message.getSender());
    }

    private Label createLabel(ChatUpdate message) {
        String sender = message.getSender();
        String text = message.getText();
        String receiver = message.getReceiver();

        if (sender.equals(guInterface.getNickname())) sender = "you";
        if (receiver.equals(guInterface.getNickname())) receiver = "you";

        return new Label(sender + " sent to " + receiver + ": " + text);
    }

    /**
     * Notifies the client that the message has been rejected from the server.
     */
    public void rejectedMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Warning!");
        alert.setContentText("Your message has been rejected!");
        alert.showAndWait();
    }

    public void disconnectedPlayer(String nickname) {
        disconnectedPlayers.add(nickname);
        updatePlayerMenuColors();
    }

    public void reconnectedPlayer(String nickname) {
        disconnectedPlayers.remove(nickname);
        updatePlayerMenuColors();
    }

    private void updatePlayerMenuColors() {
        playerMenu.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (!disconnectedPlayers.contains(item))
                        setTextFill(Color.BLACK);
                    else
                        setTextFill(Color.DARKRED);
                }
            }
        });

        playerMenu.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    if (!disconnectedPlayers.contains(item))
                        setTextFill(Color.BLACK);
                    else
                        setTextFill(Color.DARKRED);
                }
            }
        });
    }
}
