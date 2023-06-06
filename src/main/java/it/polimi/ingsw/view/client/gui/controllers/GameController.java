package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.client.BookshelfInsertion;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private final static double selectedOpacity = 0.3;
    private final static double notSelectedOpacity = 1.0;
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

    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }

    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
        currentPlayerLabel.setText("It's " + currentPlayer + "'s turn!");
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        nicknameLabel.setText("Hi " + nickname + " !");
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
        if (!guInterface.getNickname().equals(currentPlayer)) return; //TODO: show an error on gui; not your turn
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(notSelectedOpacity);
            return;
        }
        if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(selectedOpacity);
            return; //this will be used; don't delete
        }
        //TODO: show an error on gui; already 3 tiles selected
    }

    public void selectFromLivingRoom() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)) return; //TODO: show an error on gui; not your turn
        if (selectedItems.size() < 1) return; //TODO: show an error on gui; select at least one item
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
        if (!currentPlayer.equals(nickname)) return; //TODO: show an error on gui; not your turn
        if (chosenItems.size() == 0) return; //TODO: show an error on gui; select items from livingRoom
        if (!(orderItems.size() == selectedItems.size())) return; //TODO: show an error on gui; select all the items
        selectedColumn = column;
        guInterface.insertInBookshelf(new BookshelfInsertion(selectedColumn, selectedOrder));
    }

    public void insertItems() {
        for (ImageView item : orderItems) {
            bookshelfGridPane.add(item, selectedColumn, findFreeRowBookshelf(bookshelfGridPane, selectedColumn));
            item.setOpacity(notSelectedOpacity);
            item.setFitWidth(cellSizeBookshelf);
            item.setFitHeight(cellSizeBookshelf);
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

        if (numberPlayers > 2) {
            secondBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            secondBookshelfLabel.setText(nicknames.get(1));
            otherPlayersBookshelf.put(nicknames.get(1), secondBookshelfGridPane);

        }
        if (numberPlayers > 3) {
            thirdBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            thirdBookshelfLabel.setText(nicknames.get(2));
            otherPlayersBookshelf.put(nicknames.get(2), thirdBookshelfGridPane);
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

        stage.setOnCloseRequest(event -> {event.consume();
            exitChat(stage);});
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
}
