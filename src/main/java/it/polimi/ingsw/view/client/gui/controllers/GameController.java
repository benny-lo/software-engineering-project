package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.view.CommonGoalCardDescription;
import it.polimi.ingsw.utils.Item;
import it.polimi.ingsw.utils.Position;
import it.polimi.ingsw.utils.message.client.BookshelfInsertion;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GameControllerStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * Class representing Game controller.
 * The client can interact with the LivingRoom, the Bookshelf and the Chat.
 * Shows the client's Bookshelf and PersonalGoalCard, the other players Bookshelves, the LivingRoom, the current player,
 * the ending token, the CommonGoalCards, and the ranking.
 */
public class GameController extends AbstractController {
    private static final String CUP= "gui/myShelfieImages/item_tiles/Trofei1.1.png";
    private static final String CAT= "gui/myShelfieImages/item_tiles/Gatti1.1.png";
    private static final String BOOK= "gui/myShelfieImages/item_tiles/Libri1.1.png";
    private static final String PLANT= "gui/myShelfieImages/item_tiles/Piante1.1.png";
    private static final String GAME= "gui/myShelfieImages/item_tiles/Giochi1.1.png";
    private static final String FRAME= "gui/myShelfieImages/item_tiles/Cornici1.1.png";
    private static final int NUMBER_SELECTED_ITEMS = 3;
    private static final int CELL_SIZE_LIVING_ROOM = 39;
    private static final int CELL_SIZE_OTHERS_BOOKSHELF = 14;
    private static final int CELL_SIZE_BOOKSHELF = 29;
    private static final int LIVING_ROOM_GAP = 0;
    private static final int BOOKSHELF_GAP = 0;
    private static final int OTHERS_BOOKSHELF_GAP = 0;
    private static final double SELECTED_OPACITY = 0.3;
    private static final double NOT_SELECTED_OPACITY = 1.0;
    private final List<Position> selectedItems = new ArrayList<>(NUMBER_SELECTED_ITEMS);
    private String nickname;
    private int numberActualPlayers;
    private String currentPlayer;
    private final Map<String, GridPane> otherPlayersBookshelf = new HashMap<>();
    private final Map<String, ImageView> otherPlayersEndingToken = new HashMap<>();
    private List<Item> chosenItems = new ArrayList<>(NUMBER_SELECTED_ITEMS);
    private final List<ImageView> orderItems = new ArrayList<>(NUMBER_SELECTED_ITEMS);
    private final List<Integer> selectedOrder = new ArrayList<>(NUMBER_SELECTED_ITEMS);
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    private final Stage chatStage = new Stage();
    private final Map <String, Integer> scores = new HashMap<>();
    private GameControllerStatus status = GameControllerStatus.WAITING;
    private final Alert errorAlert = new Alert(Alert.AlertType.ERROR);
    private int firstCommonGoalCardId;
    private int secondCommonGoalCardId;
    private boolean firstCommonGoalCardUpdate = true;
    @FXML
    private GridPane livingRoomGridPane;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private ImageView personalGoalCardImageView;
    @FXML
    private Label firstBookshelfLabel;
    @FXML
    private Label secondBookshelfLabel;
    @FXML
    private Label thirdBookshelfLabel;
    @FXML
    private ImageView firstBookshelfImageView;
    @FXML
    private ImageView secondBookshelfImageView;
    @FXML
    private ImageView thirdBookshelfImageView;
    @FXML
    private GridPane firstBookshelfGridPane;
    @FXML
    private GridPane secondBookshelfGridPane;
    @FXML
    private GridPane thirdBookshelfGridPane;
    @FXML
    private ImageView firstCommonGoalCardTopImageView;
    @FXML
    private ImageView secondCommonGoalCardTopImageView;
    @FXML
    private ImageView firstCommonGoalCardImageView;
    @FXML
    private ImageView secondCommonGoalCardImageView;
    @FXML
    private ImageView firstChosenItemImageView;
    @FXML
    private ImageView secondChosenItemImageView;
    @FXML
    private ImageView thirdChosenItemImageView;
    @FXML
    private Label firstChosenItemLabel;
    @FXML
    private Label secondChosenItemLabel;
    @FXML
    private Label thirdChosenItemLabel;
    @FXML
    private Button insertColumn0Button;
    @FXML
    private Button insertColumn1Button;
    @FXML
    private Button insertColumn2Button;
    @FXML
    private Button insertColumn3Button;
    @FXML
    private Button insertColumn4Button;
    @FXML
    private GridPane bookshelfGridPane;
    @FXML
    private ImageView currentEndingTokenImageView;
    @FXML
    private ImageView firstEndingTokenImageView;
    @FXML
    private ImageView secondEndingTokenImageView;
    @FXML
    private ImageView thirdEndingTokenImageView;
    @FXML
    private Label rankingLabel;

    /**
     * Sets {@code this} in the {@code GUInterface}.
     * Sets the gap between LivingRoom tiles.
     * Sets the gap between client Bookshelf tiles.
     * Sets the gap between Bookshelves tiles.
     * Sets the columns buttons.
     * Sets the CommonGoalCard {@code ImageView}s.
     * Creates the Chat {@code Stage} and loads the Chat {@code scene}.
     */
    @FXML
    public void initialize() {
        guInterface.setController(this);
        livingRoomGridPane.setHgap(LIVING_ROOM_GAP);
        livingRoomGridPane.setVgap(LIVING_ROOM_GAP);
        bookshelfGridPane.setHgap(BOOKSHELF_GAP);
        bookshelfGridPane.setVgap(BOOKSHELF_GAP);
        firstBookshelfGridPane.setHgap(OTHERS_BOOKSHELF_GAP);
        firstBookshelfGridPane.setVgap(OTHERS_BOOKSHELF_GAP);
        secondBookshelfGridPane.setHgap(OTHERS_BOOKSHELF_GAP);
        secondBookshelfGridPane.setVgap(OTHERS_BOOKSHELF_GAP);
        thirdBookshelfGridPane.setHgap(OTHERS_BOOKSHELF_GAP);
        thirdBookshelfGridPane.setVgap(OTHERS_BOOKSHELF_GAP);
        insertColumn0Button.setOnMouseClicked(mouseEvent -> selectColumn(0));
        insertColumn1Button.setOnMouseClicked(mouseEvent -> selectColumn(1));
        insertColumn2Button.setOnMouseClicked(mouseEvent -> selectColumn(2));
        insertColumn3Button.setOnMouseClicked(mouseEvent -> selectColumn(3));
        insertColumn4Button.setOnMouseClicked(mouseEvent -> selectColumn(4));
        firstCommonGoalCardImageView.setOnMouseClicked(mouseEvent -> showCommonGoalCardDescription(firstCommonGoalCardId));
        secondCommonGoalCardImageView.setOnMouseClicked(mouseEvent -> showCommonGoalCardDescription(secondCommonGoalCardId));
        try {
            createChat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the current player.
     * @param currentPlayer The nickname of the player to set as current.
     */
    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(nickname)) {
            status = GameControllerStatus.LIVING_ROOM;
            currentPlayerLabel.setText("It's your turn!");
        } else {
            currentPlayerLabel.setText("It's " + currentPlayer + "'s turn!");
        }
    }

    /**
     * Sets the nickname of the client.
     * @param nickname The nickname of the client.
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
        nicknameLabel.setText("Hi " + nickname + "!");
    }

    private Image getImage(Item item) {
        String image;
        if(item == null) return null;
        switch (item) {
            case CAT -> image = CAT;
            case CUP -> image = CUP;
            case FRAME -> image = FRAME;
            case GAME -> image = GAME;
            case PLANT -> image = PLANT;
            case BOOK -> image = BOOK;
            default -> image = null;
        }
        if(image == null) return null;
        return new Image(image);
    }

    private int getRealRow(int row) {
        return 5 - row;
    }
    //ENDGAME

    /**
     * Notifies the client that has been disconnected from the server.
     */
    public void disconnectionInGame() {
        errorAlert.setHeaderText("Error!");
        errorAlert.setContentText("You have been disconnected from the server.\n");
        errorAlert.showAndWait();
        System.exit(0);
    }

    /**
     * Notifies the client that a player has disconnected, and the game has ended.
     */
    public void playerDisconnectionInGame() {
        errorAlert.setHeaderText("Error!");
        errorAlert.setContentText("A player has disconnected, the game has ended.\n");
        errorAlert.showAndWait();
        System.exit(0);
    }

    /**
     * Notifies the client that the game has ended, and shows the winner and the ranking.
     * @param winner The player that has won.
     */
    public void endGame(String winner){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("The Game has ended!");
        alert.setContentText("The winner is: " + winner + ".\nRanking: ");
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            alert.setContentText(alert.getContentText() + "\n" + e.getKey() + ": " + e.getValue() + " points");
        }
        alert.showAndWait();
        System.exit(0);
    }

    //GRID

    /**
     * Sets the {@code Item}s in the LivingRoom.
     * @param livingRoom A {@code Map} of the LivingRoom.
     */
    public void setLivingRoomGridPane(Map<Position, Item> livingRoom) {
        if (livingRoom == null) return;
        for (Position position : livingRoom.keySet()) {
            setLivingRoomTile(livingRoom.get(new Position(position.getRow(), position.getColumn())), position.getColumn(), position.getRow());
        }
    }

    private void setLivingRoomTile(Item item, int column, int row) {
        ImageView imageView = new ImageView(getImage(item));
        if (imageView.getImage() == null)
            clearNodeByColumnRow(column, row);
        imageView.setOnMouseClicked(mouseEvent -> selectItem(new Position(row, column)));
        imageView.setFitWidth(CELL_SIZE_LIVING_ROOM);
        imageView.setFitHeight(CELL_SIZE_LIVING_ROOM);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

    /**
     * Selects or deselects the {@code Item} clicked by the user in the LivingRoom.
     * @param position {@code Position} selected by the user.
     */
    public void selectItem(Position position) {
        if (!guInterface.getNickname().equals(currentPlayer)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.LIVING_ROOM)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }

        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).forEach(n -> n.setOpacity(NOT_SELECTED_OPACITY));
        } else if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).forEach(n -> n.setOpacity(SELECTED_OPACITY));
        } else {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have already selected 3 items!");
            warningAlert.showAndWait();
        }
    }

    /**
     * Sends the {@code Item}s selected to the {@code GUInterface}.
     * @throws IOException Error in I/O.
     */
    public void selectFromLivingRoom() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.LIVING_ROOM)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (selectedItems.isEmpty()){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select at least one item!");
            warningAlert.showAndWait();
            return;
        }

        guInterface.selectFromLivingRoom(new LivingRoomSelection(new ArrayList<>(selectedItems)));
    }

    /**
     * Sets the chosen {@code Item}s in the {@code ImageView}s above the Bookshelf.
     * @param chosenItems {@code List} of chosen {@code Item}s.
     */
    public void setChosenItems(List<Item> chosenItems){
        if (!currentPlayer.equals(nickname)) return;
        status = GameControllerStatus.BOOKSHELF;
        this.chosenItems = chosenItems;
        updateChosenItemsImageView();
    }

    private void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    /**
     * Clears the chosen tiles in the LivingRoom.
     */
    public void clearTilesList() {
        if (!currentPlayer.equals(nickname)) return;
        for (Position items : selectedItems) clearNodeByColumnRow(items.getColumn(), items.getRow());
    }

    /**
     * Resets the opacity of the selected tiles in the LivingRoom when the selection is gone wrong.
     */
    public void resetOpacity() {
        for (Position position : selectedItems) {
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).forEach(n -> n.setOpacity(NOT_SELECTED_OPACITY));
        }
    }

    /**
     * Clears the {@code selectedItems} when the items selection is gone wrong.
     */
    public void clearSelectedItems() {
        selectedItems.clear();
    }

    /**
     * Notifies the client that the items selection is gone wrong.
     */
    public void failedSelection() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed selection, retry!");
        warningAlert.showAndWait();
    }

    //BOOKSHELF

    private void updateChosenItemsImageView() {
        firstChosenItemImageView.setImage(getImage(chosenItems.get(0)));
        firstChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(firstChosenItemImageView));
        if (chosenItems.size() > 1) {
            secondChosenItemImageView.setImage(getImage(chosenItems.get(1)));
            secondChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(secondChosenItemImageView));
        }
        if (chosenItems.size() > 2) {
            thirdChosenItemImageView.setImage(getImage(chosenItems.get(2)));
            thirdChosenItemImageView.setOnMouseClicked(mouseEvent -> this.selectOrder(thirdChosenItemImageView));
        }
    }

    /**
     * Orders the {@code ImageView}s selected.
     * @param selected The {@code ImageView} selected.
     */
    public void selectOrder(ImageView selected) {
        if (!guInterface.getNickname().equals(currentPlayer)) return;
        if (!status.equals(GameControllerStatus.BOOKSHELF)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (!orderItems.contains(selected)) {
            orderItems.add(selected);
            selected.setOpacity(SELECTED_OPACITY);
        } else {
            orderItems.remove(selected);
            selected.setOpacity(NOT_SELECTED_OPACITY);
        }
        updateOrderItems();
    }

    private void updateOrderItems() {
        boolean first = false, second = false, third = false;

        for (ImageView imageView : orderItems) {
            if (imageView.equals(firstChosenItemImageView)) {
                firstChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                first = true;
            } else if (imageView.equals(secondChosenItemImageView)) {
                secondChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                second = true;
            } else if (imageView.equals(thirdChosenItemImageView)) {
                thirdChosenItemLabel.setText(String.valueOf(orderItems.indexOf(imageView)+1));
                third = true;
            }
        }

        selectedOrder.clear();

        if (orderItems.contains(firstChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(firstChosenItemImageView));
        if (orderItems.contains(secondChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(secondChosenItemImageView));
        if (orderItems.contains(thirdChosenItemImageView))
            selectedOrder.add(orderItems.indexOf(thirdChosenItemImageView));

        if (!first)
            firstChosenItemLabel.setText("");
        if (!second)
            secondChosenItemLabel.setText("");
        if (!third)
            thirdChosenItemLabel.setText("");
    }

    /**
     * Sends to {@code GUInterface} the column selected by the client.
     * @param column The column selected.
     */
    public void selectColumn(int column) {
        if (!currentPlayer.equals(nickname)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (!status.equals(GameControllerStatus.BOOKSHELF)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You cannot do this now!");
            warningAlert.showAndWait();
            return;
        }
        if (chosenItems.isEmpty()) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select items from the Living Room!");
            warningAlert.showAndWait();
            return;
        }
        if (orderItems.size() != selectedItems.size()) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to choose an order for all the items!");
            warningAlert.showAndWait();
            return;
        }
        guInterface.insertInBookshelf(new BookshelfInsertion(column, new ArrayList<>(selectedOrder)));
    }

    /**
     * Inserts the {@code ImageView}s in the selected order in the Bookshelf, when the insertion is successful.
     */
    public void insertItems() {
        for (ImageView item : orderItems) {
            ImageView imageView = new ImageView(item.getImage());
            imageView.setOpacity(NOT_SELECTED_OPACITY);
            imageView.setFitWidth(CELL_SIZE_BOOKSHELF);
            imageView.setFitHeight(CELL_SIZE_BOOKSHELF);
            clearChosenItemLabels();
        }
        endTurnClear();
        status = GameControllerStatus.WAITING;
        currentPlayerLabel.setText("Waiting for other players...");
    }

    private void clearChosenItemLabels() {
        firstChosenItemLabel.setText("");
        secondChosenItemLabel.setText("");
        thirdChosenItemLabel.setText("");
    }

    private void endTurnClear() {
        selectedItems.clear();
        selectedOrder.clear();
        chosenItems.clear();
        orderItems.clear();
        firstChosenItemImageView.setOnMouseClicked(null);
        secondChosenItemImageView.setOnMouseClicked(null);
        thirdChosenItemImageView.setOnMouseClicked(null);
    }

    /**
     * Clears the {@code ImageView}s above the Bookshelf, when the insertion is successful.
     */
    public void clearChosenItemsImageView() {
        firstChosenItemImageView.setImage(null);
        firstChosenItemImageView.setOpacity(NOT_SELECTED_OPACITY);
        secondChosenItemImageView.setImage(null);
        secondChosenItemImageView.setOpacity(NOT_SELECTED_OPACITY);
        thirdChosenItemImageView.setImage(null);
        thirdChosenItemImageView.setOpacity(NOT_SELECTED_OPACITY);
    }

    /**
     * Notifies the client that the insertion is gone wrong.
     */
    public void failedInsertion() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed insertion, retry!");
        warningAlert.showAndWait();
    }

    //PERSONAL GOAL CARD

    /**
     * Loads the client's PersonalGoalCard.
     * @param id The ID of the PersonalGoalCard.
     */
    public void setPersonalGoalCard(int id){
        personalGoalCardImageView.setImage(new Image("/gui/myShelfieImages/personal_goal_cards/personal_goal_card_" + id + ".png"));
    }

    //BOOKSHELVES

    /**
     * Initializes the Bookshelves of the other players.
     * @param nicknames The nicknames of the other players.
     */
    public void initializeBookshelves(List<String> nicknames) {
        int numberPlayers = nicknames.size();
        numberActualPlayers = nicknames.size();
        nicknames.remove(nickname);

        firstBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
        firstBookshelfLabel.setText(nicknames.get(0));
        otherPlayersBookshelf.put(nicknames.get(0), firstBookshelfGridPane);
        otherPlayersEndingToken.put(nicknames.get(0), firstEndingTokenImageView);

        if (numberPlayers > 2) {
            secondBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            secondBookshelfLabel.setText(nicknames.get(1));
            otherPlayersBookshelf.put(nicknames.get(1), secondBookshelfGridPane);
            otherPlayersEndingToken.put(nicknames.get(1), secondEndingTokenImageView);

        }
        if (numberPlayers > 3) {
            thirdBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            thirdBookshelfLabel.setText(nicknames.get(2));
            otherPlayersBookshelf.put(nicknames.get(2), thirdBookshelfGridPane);
            otherPlayersEndingToken.put(nicknames.get(2), thirdEndingTokenImageView);
        }
    }

    /**
     * Updates the Bookshelf of a player.
     * @param owner The owner of the Bookshelf.
     * @param update The new updated Bookshelf.
     */
    public void updateBookshelf(String owner, Map<Position, Item> update) {
        if (owner == null) return;
        if (update == null) return;

        GridPane bookshelf = (owner.equals(nickname)) ? bookshelfGridPane : otherPlayersBookshelf.get(owner);
        int size = (owner.equals(nickname)) ? CELL_SIZE_BOOKSHELF : CELL_SIZE_OTHERS_BOOKSHELF;

        for (Map.Entry<Position, Item> e: update.entrySet()) {
            ImageView imageView = new ImageView(getImage(e.getValue()));
            imageView.setFitHeight(size);
            imageView.setFitWidth(size);
            bookshelf.add(imageView, e.getKey().getColumn(), getRealRow(e.getKey().getRow()));
        }
    }

    //COMMON GOAL CARDS

    /**
     * Sets the CommonGoalCard descriptions, top tokens and {@code ImageView}s the first time that is called.
     * Afterwards it only updates the top tokens.
     * @param commonGoalCards A {@code Map} of the CommonGoalCards.
     */
    public void updateCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null) return;

        String filename;
        String topName;
        boolean first = true;

        //setting commonGoalCards in a map only when the game starts
        if (firstCommonGoalCardUpdate) {
            firstCommonGoalCardUpdate = false;
            for (Integer id : commonGoalCards.keySet()) {
                if (first) {
                    firstCommonGoalCardId = id;
                    first = false;
                }
                else
                    secondCommonGoalCardId = id;
            }
        }

        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            filename = "/gui/myShelfieImages/common_goal_cards/common_goal_card_" + card.getKey() + ".jpg";
            topName = "/gui/myShelfieImages/scoring_tokens/scoring_" + card.getValue() + ".jpg";
            if (firstCommonGoalCardId == card.getKey()) {
                firstCommonGoalCardTopImageView.setImage(new Image(topName));
                firstCommonGoalCardImageView.setImage(new Image(filename));
            } else if (secondCommonGoalCardId == card.getKey()) {
                secondCommonGoalCardTopImageView.setImage(new Image(topName));
                secondCommonGoalCardImageView.setImage(new Image(filename));
            }
        }
    }

    private void showCommonGoalCardDescription(int id) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setHeaderText("Common Goal Card Description");
        infoAlert.setContentText(CommonGoalCardDescription.getDescription(id));
        infoAlert.showAndWait();
    }

    //ENDING TOKEN

    /**
     * Sets the ending token.
     * @param owner The owner of the ending token.
     */
    public void setEndingToken(String owner) {
        if (owner == null) return;
        if (owner.equals(nickname))
            currentEndingTokenImageView.setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
        else
            otherPlayersEndingToken.get(owner).setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
    }


    //SCORES

    /**
     * Sets the scores of the players in the {@code rankingLabel}.
     * @param newScores A {@code Map} of the new scores.
     */
    public void setScores(Map<String, Integer> newScores) {
        if (newScores == null) return;
        this.scores.putAll(newScores);
        rankingLabel.setText("");
        for(Map.Entry<String, Integer> e : scores.entrySet()) {
            rankingLabel.setText(rankingLabel.getText() + e.getKey() + ": " + e.getValue() + " points\n");
        }
    }

    //CHAT
    private void createChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Chat.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        chatStage.setScene(scene);
        chatStage.setTitle("MyShelfieChat");
        chatStage.hide();
        chatStage.setResizable(false);
        chatStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/gui/myShelfieImages/publisher_material/icon_50x50px.png"))));

        chatStage.setOnCloseRequest(event -> {
            event.consume();
            chatStage.hide();
        });
    }

    /**
     * Shows the Chat {@code Stage} when the {@code enterChatButton} is clicked.
     */
    public void enterChat() {
        chatStage.show();
    }


    //DISCONNECTION AND RECONNECTION

    /**
     * Notifies that a player has reconnected, and it adds back his bookshelf.
     * @param nickname The nickname of the player that has reconnected.
     */
    public void reconnectionInGame(String nickname) {
        numberActualPlayers++;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Reconnection");
        alert.setContentText(nickname + " has reconnected!");

        if (nickname.equals(firstBookshelfLabel.getText())) {
            firstBookshelfImageView.setOpacity(NOT_SELECTED_OPACITY);
            firstBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(NOT_SELECTED_OPACITY));
            firstBookshelfLabel.setTextFill(Color.BLACK);
        }

        if (nickname.equals(secondBookshelfLabel.getText())) {
            secondBookshelfImageView.setOpacity(NOT_SELECTED_OPACITY);
            secondBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(NOT_SELECTED_OPACITY));
            secondBookshelfLabel.setTextFill(Color.BLACK);
        }

        if (nickname.equals(thirdBookshelfLabel.getText())) {
            thirdBookshelfImageView.setOpacity(NOT_SELECTED_OPACITY);
            thirdBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(NOT_SELECTED_OPACITY));
            thirdBookshelfLabel.setTextFill(Color.BLACK);
        }

        alert.showAndWait();
    }

    /**
     * Notifies that a player has disconnected, and it removes his bookshelf. If only one player has remained, it notifies
     * that the game will end in 30 seconds.
     * @param nickname The nickname of the player that has disconnected.
     */
    public void disconnectionInGame(String nickname) {
        numberActualPlayers--;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Disconnection");
        alert.setContentText(nickname + " has disconnected!");

        if (nickname.equals(firstBookshelfLabel.getText())) {
            firstBookshelfImageView.setOpacity(SELECTED_OPACITY);
            firstBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(SELECTED_OPACITY));
            firstBookshelfLabel.setTextFill(Color.DARKRED);
        }

        if (nickname.equals(secondBookshelfLabel.getText())) {
            secondBookshelfImageView.setOpacity(SELECTED_OPACITY);
            secondBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(SELECTED_OPACITY));
            secondBookshelfLabel.setTextFill(Color.DARKRED);
        }

        if (nickname.equals(thirdBookshelfLabel.getText())) {
            thirdBookshelfImageView.setOpacity(SELECTED_OPACITY);
            thirdBookshelfGridPane.getChildren().forEach(n -> n.setOpacity(SELECTED_OPACITY));
            thirdBookshelfLabel.setTextFill(Color.DARKRED);
        }

        alert.showAndWait();

        if (numberActualPlayers == 1) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText("You are the only one left in the game.");
            alert1.setContentText("If you end your turn and nobody gets back into the game,\nit will ends in 30 seconds.");
            alert1.showAndWait();
        }
    }
}