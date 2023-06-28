package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.message.client.ChatMessage;
import it.polimi.ingsw.utils.message.server.ChatUpdate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChatController extends AbstractController {
    private final static String BROADCAST = "all";
    @FXML
    private ComboBox<String> playerMenu;
    @FXML
    private VBox messagesDisplayed;
    @FXML
    private TextField textToSend;

    private List<ChatUpdate> messages;

    @FXML
    public void initialize() {
        guInterface.receiveController(this);
        messages = new ArrayList<>();
        playerMenu.getItems().add("all");
        playerMenu.setValue("all");

        List<String> nicknames = guInterface.getOthersNicknames();
        for(String nick : nicknames) {
            playerMenu.getItems().add(nick);
        }
    }

    public void receiveMessage(ChatUpdate message) {
        messages.add(message);
        if (isToDisplay(message)) {
            messagesDisplayed.getChildren().add(createLabel(message));
        }
    }

    public void onSendButtonClick() {
        String text = textToSend.getText();
        String receiver = playerMenu.getValue();
        ChatMessage message = new ChatMessage(text, receiver);

        textToSend.clear();

        guInterface.writeChat(message);
    }

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

    public void rejectedMessage() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Warning!");
        alert.setContentText("Your message has been rejected!");
        alert.showAndWait();
    }
}
