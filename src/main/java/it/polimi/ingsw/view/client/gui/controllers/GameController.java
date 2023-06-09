package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.utils.game.Item;
import it.polimi.ingsw.utils.game.Position;
import it.polimi.ingsw.utils.message.client.BookshelfInsertion;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.utils.gui.CloseWindow.exitChat;


public class GameController implements Initializable {
    private static GUInterface guInterface;
    final private static String CUP= "gui/myShelfieImages/item_tiles/Trofei1.1.png";
    final private static String CAT= "gui/myShelfieImages/item_tiles/Gatti1.1.png";
    final private static String BOOK= "gui/myShelfieImages/item_tiles/Libri1.1.png";
    final private static String PLANT= "gui/myShelfieImages/item_tiles/Piante1.1.png";
    final private static String GAME= "gui/myShelfieImages/item_tiles/Giochi1.1.png";
    final private static String FRAME= "gui/myShelfieImages/item_tiles/Cornici1.1.png";
    private final List<Position> selectedItems = new ArrayList<>(numberSelectedItems);
    private String nickname;
    private String currentPlayer;
    private int numberPlayers;
    private final static int numberSelectedItems = 3;
    private final Map<String, GridPane> otherPlayersBookshelf = new HashMap<>();
    private final Map<String, ImageView> otherPlayersEndingToken = new HashMap<>();
    private final static int cellSizeLivingRoom = 60;
    private final static int cellSizeOthersBookshelf = 15;
    private final static int cellSizeBookshelf = 50;
    private final static int livingRoomGap = 5;
    private final static int bookshelfGap = 7;
    private final static int othersBookshelfGap = 2;
    private int bookshelvesRows;
    private int bookshelvesColumns;
    private List<Item> chosenItems = new ArrayList<>(numberSelectedItems);
    private final List<ImageView> orderItems = new ArrayList<>(numberSelectedItems);
    private final List<Integer> selectedOrder = new ArrayList<>(numberSelectedItems);
    private int selectedColumn;
    private boolean chat;
    private final static double selectedOpacity = 0.3;
    private final static double notSelectedOpacity = 1.0;
    private final Alert warningAlert = new Alert(Alert.AlertType.WARNING);
    @FXML
    private GridPane livingRoomGridPane;
    @FXML
    public Button sendButton;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Button enterChatButton;
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
    private Label firstCommonGoalCardId;
    @FXML
    private Label secondCommonGoalCardId;
    @FXML
    private Label firstCommonGoalCardTop;
    @FXML
    private Label secondCommonGoalCardTop;
    @FXML
    private Label firstCommonGoalCardDescription;
    @FXML
    private Label secondCommonGoalCardDescription;
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
    private Label rankingsLabel;

    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }

    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
        if (currentPlayer.equals(nickname))
            currentPlayerLabel.setText("It's your turn!");
        else
            currentPlayerLabel.setText("It's " + currentPlayer + "'s turn!");
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        nicknameLabel.setText("Hi " + nickname + "!");
    }

    private Image getImage(Item item) {
        String image;
        if(item==null) return null;
        switch (item){
            case CAT -> image=CAT;
            case CUP -> image=CUP;
            case FRAME -> image=FRAME;
            case GAME -> image=GAME;
            case PLANT -> image=PLANT;
            case BOOK -> image=BOOK;
            default -> image=null;
        }
        if(image==null) return null;
        return new Image(image);
    }

    private int getRealRow(int row) {
        if (row == 0) return 5;
        if (row == 1) return 4;
        if (row == 2) return 3;
        if (row == 3) return 2;
        if (row == 4) return 1;
        return 0;
    }

    public void disconnectionInGame() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error!");
        alert.setContentText("You have been disconnected from the server.\n");
        alert.showAndWait();
        System.exit(0);
    }

    //GRID
    public void setLivingRoomGridPane(Item[][] livingRoom){
        if(livingRoom ==null) return;
        livingRoomGridPane.getChildren().clear();
        for(int i = 0; i< livingRoom.length; i++){
            for(int j = 0; j< livingRoom.length; j++){
                setLivingRoomTile(livingRoom[i][j], j, i);
            }
        }
    }

    private void setLivingRoomTile(Item item, int column, int row){
        ImageView imageView = new ImageView(getImage(item));
        imageView.setOnMouseClicked(mouseEvent -> selectItem(new Position(row, column)));
        imageView.setFitWidth(cellSizeLivingRoom);
        imageView.setFitHeight(cellSizeLivingRoom);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

    public void selectItem(Position position) {
        if (!guInterface.getNickname().equals(currentPlayer)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(notSelectedOpacity);
            return;
        }
        if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(selectedOpacity);
            return;
        }
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("You have already selected 3 items!");
        warningAlert.showAndWait();
    }

    public void selectFromLivingRoom() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (selectedItems.size() < 1){
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select at least one item!");
            warningAlert.showAndWait();
            return;
        }
        System.out.println("lato client select funzionante\n" + selectedItems);

        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
    }

    public void setChosenItems(List<Item> itemsChosen){
        if (!currentPlayer.equals(nickname)) return;
        this.chosenItems = itemsChosen;
        updateChosenItemsImageView();
    }

    private void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    public void clearTilesList() {
        for (Position items : selectedItems) clearNodeByColumnRow(items.getColumn(), items.getRow());
    }

    public void resetOpacity() {
        for (Position position : selectedItems) {
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(notSelectedOpacity);
        }
    }

    public void clearSelectedItems() {
        selectedItems.clear();
    }

    public void failedSelection() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed selection, retry!");
        warningAlert.showAndWait();

    }

    //BOOKSHELF

    private void updateChosenItemsImageView() {
        System.out.println("sto settando le immagini");
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

    public void selectOrder(ImageView selected) {
        if (!guInterface.getNickname().equals(currentPlayer)) return;
        if (!orderItems.contains(selected)) {
            orderItems.add(selected);
            selected.setOpacity(selectedOpacity);
        } else {
            orderItems.remove(selected);
            selected.setOpacity(notSelectedOpacity);
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
    public void selectColumn(int column) {
        if (!currentPlayer.equals(nickname)) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("Wait for your turn!");
            warningAlert.showAndWait();
            return;
        }
        if (chosenItems.size() == 0) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to select items from the Living Room!");
            warningAlert.showAndWait();
            return;
        }
        if (!(orderItems.size() == selectedItems.size())) {
            warningAlert.setHeaderText("Warning!");
            warningAlert.setContentText("You have to choose an order for all the items!");
            warningAlert.showAndWait();
            return;
        }
        selectedColumn = column;
        guInterface.insertInBookshelf(new BookshelfInsertion(selectedColumn, selectedOrder));
    }

    public void insertItems() {
        for (ImageView item : orderItems) {
            ImageView imageView = new ImageView(item.getImage());
            bookshelfGridPane.add(imageView, selectedColumn, findFreeRowBookshelf(bookshelfGridPane, selectedColumn));
            imageView.setOpacity(notSelectedOpacity);
            imageView.setFitWidth(cellSizeBookshelf);
            imageView.setFitHeight(cellSizeBookshelf);
            clearChosenItemLabels();
        }
        endTurnClear();
    }

    private int findFreeRowBookshelf(GridPane bookshelfGridPane, int column) {
        for (int i = bookshelvesRows - 1; i >= 0; i--) {
            int finalI = i;
            if (bookshelfGridPane.getChildren().stream().noneMatch(n -> (GridPane.getColumnIndex(n) == column && GridPane.getRowIndex(n) == finalI)))
                return finalI;
        }
        return -1;
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
    }

    public void clearChosenItemsImageView() {
        firstChosenItemImageView.setImage(null);
        secondChosenItemImageView.setImage(null);
        thirdChosenItemImageView.setImage(null);
    }

    public void failedInsertion() {
        warningAlert.setHeaderText("Warning!");
        warningAlert.setContentText("Failed insertion, retry!");
        warningAlert.showAndWait();
    }

    //PERSONAL GOAL CARD

    public void setPersonalGoalCard(int id){
        personalGoalCardImageView.setImage(new Image("/gui/myShelfieImages/personal_goal_cards/personal_goal_card_" + id + ".png"));
    }

    //BOOKSHELVES

    public void initializeBookshelves(List<String> nicknames, int bookshelvesRows, int bookshelvesColumns) {
        numberPlayers = nicknames.size();
        this.bookshelvesRows = bookshelvesRows;
        this.bookshelvesColumns = bookshelvesColumns;
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

    public void updateBookshelf(String owner, Map<Position, Item> bookshelf) {
        if (owner == null) return;
        if (bookshelf == null) return;
        if (!otherPlayersBookshelf.containsKey(owner)) return;
        for (Position position : bookshelf.keySet()) {
            ImageView imageView = new ImageView(getImage(bookshelf.get(position)));
            imageView.setFitHeight(cellSizeOthersBookshelf);
            imageView.setFitWidth(cellSizeOthersBookshelf);
            otherPlayersBookshelf.get(owner).add(imageView, position.getColumn(), getRealRow(position.getRow()));
        }
    }

    //COMMON GOAL CARDS

    public void updateCommonGoalCards(Map<Integer, Integer> commonGoalCards) {
        if (commonGoalCards == null)
            return;
        String description = "";
        boolean first = true;
        for (Map.Entry<Integer, Integer> card : commonGoalCards.entrySet()) {
            switch (card.getKey()){
                case 0 -> description="Two groups each containing 4 tiles of\nthe same type in a 2x2 square.\nThe tiles of one square can be different\nfrom those of the other square.";
                case 1 -> description="Two columns each formed by 6 different\ntypes of tiles.";
                case 2 -> description="Four groups each containing at least 4\ntiles of the same type.\nThe tiles of one group can be different\nfrom those of another group.";
                case 3 -> description="Six groups each containing at least 2\ntiles of the same type\nThe tiles of one group can be different\nfrom those of another group.";
                case 4 -> description="Three columns each formed by 6 tiles\nof maximum three different types.\nOne column can show the same or\na different combination of another column";
                case 5 -> description="Two lines each formed by 5 different\ntypes of tiles. One line can\nshow the same or a different\ncombination of the other line.";
                case 6 -> description="Four lines each formed by 5 tiles of\nmaximum three different types. One \nline can show the same or a different\ncombination of another line.";
                case 7 -> description="Four tiles of the same type in the\nfour corners of the bookshelf.";
                case 8 -> description="Eight or more tiles of the same type\nwith no restrictions about the\nposition of these tiles.";
                case 9 -> description="Five tiles of the same type forming an X.";
                case 10 -> description="Five tiles of the same type forming a diagonal";
                case 11 -> description="Five columns of increasing or decreasing height.\nStarting from the first column\non the left or on the right,\neach next column must be made\nof exactly one more tile.\nTiles can be of any type.";
            }
            if (first) {
                firstCommonGoalCardId.setText("Id: " + card.getKey());
                firstCommonGoalCardTop.setText("Top token value: " + card.getValue());
                firstCommonGoalCardDescription.setText(description);
                first = false;
            } else {
                secondCommonGoalCardId.setText("Id: " + card.getKey());
                secondCommonGoalCardTop.setText("Top token value: " + card.getValue());
                secondCommonGoalCardDescription.setText(description);
            }
        }
    }

    //ENDING TOKEN

    public void setEndingToken(String owner) {
        if (owner == null) return;
        if (owner.equals(nickname))
            currentEndingTokenImageView.setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
        else
            otherPlayersEndingToken.get(owner).setImage(new Image("/gui/myShelfieImages/scoring_tokens/end_game.jpg"));
    }

    //RANKINGS

    public void setRankings(Map<String, Integer> scores) {
        if (scores == null) return;
        rankingsLabel.setText("");
        for(String player : scores.keySet()) {
            rankingsLabel.setText(rankingsLabel.getText() + player + ": " + scores.get(player) + " points\n");
        }
    }

    //CHAT
    public void enterChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Chat.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("MyShelfieChat");
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            chat = false;
            exitChat(stage);
        });

        chat = true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
        livingRoomGridPane.setHgap(livingRoomGap);
        livingRoomGridPane.setVgap(livingRoomGap);
        bookshelfGridPane.setHgap(bookshelfGap);
        bookshelfGridPane.setVgap(bookshelfGap);
        firstBookshelfGridPane.setHgap(othersBookshelfGap);
        firstBookshelfGridPane.setVgap(othersBookshelfGap);
        secondBookshelfGridPane.setHgap(othersBookshelfGap);
        secondBookshelfGridPane.setVgap(othersBookshelfGap);
        thirdBookshelfGridPane.setHgap(othersBookshelfGap);
        thirdBookshelfGridPane.setVgap(othersBookshelfGap);
        insertColumn0Button.setOnMouseClicked(mouseEvent -> selectColumn(0));
        insertColumn1Button.setOnMouseClicked(mouseEvent -> selectColumn(1));
        insertColumn2Button.setOnMouseClicked(mouseEvent -> selectColumn(2));
        insertColumn3Button.setOnMouseClicked(mouseEvent -> selectColumn(3));
        insertColumn4Button.setOnMouseClicked(mouseEvent -> selectColumn(4));
    }

    public boolean isChatOpen() {
        return chat;
    }
}
