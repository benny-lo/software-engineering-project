package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
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
    final private String CUP= "gui/myShelfieImages/item_tiles/Trofei1.1.png";
    final private String CAT= "gui/myShelfieImages/item_tiles/Gatti1.1.png";
    final private String BOOK= "gui/myShelfieImages/item_tiles/Libri1.1.png";
    final private String PLANT= "gui/myShelfieImages/item_tiles/Piante1.1.png";
    final private String GAME= "gui/myShelfieImages/item_tiles/Giochi1.1.png";
    final private String FRAME= "gui/myShelfieImages/item_tiles/Cornici1.1.png";
    private final List<Position> selectedItems = new ArrayList<>();
    private String nickname;
    private String currentPlayer;
    private int numberPlayers;
    private List<String> nicknames = new ArrayList<>();
    private final static int cellSizeLivingRoom = 49;
    private List<Item> chosenItems = new ArrayList<>();
    @FXML
    private GridPane livingRoomGridPane;
    @FXML
    private Button sendButton;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Button bookshelvesButton;
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

    //GRID
    public void setLivingRoomGridPane(Item[][] livingRoom){
        if(livingRoom ==null) return;
        for(int i = 0; i< livingRoom.length; i++){
            for(int j = 0; j< livingRoom.length; j++){
                setTile(livingRoom[i][j], j, i);
            }
        }
    }

    private void setTile(Item item, int column, int row){
        String image;
        if(item==null) return;
        switch (item){
            case CAT -> image=CAT;
            case CUP -> image=CUP;
            case FRAME -> image=FRAME;
            case GAME -> image=GAME;
            case PLANT -> image=PLANT;
            case BOOK -> image=BOOK;
            default -> image=null;
        }
        if(image==null) return;
        ImageView imageView= new ImageView(image);
        imageView.setOnMouseClicked(mouseEvent -> this.selectItem(new Position(row, column)));
        imageView.setFitWidth(cellSizeLivingRoom);
        imageView.setFitHeight(cellSizeLivingRoom);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

    public void selectItem(Position position) {
        if (!guInterface.getNickname().equals(currentPlayer)) return; //TODO: show an error on gui; not your turn
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(1.0);
            return;
        }
        if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0).setOpacity(0.3);
            return; //this will be used; don't delete
        }
        //TODO: show an error on gui; already 3 tiles selected
    }



    public void selectFromLivingRoom() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)) return; //TODO: show an error on gui; not your turn
        if (selectedItems.size() < 1) return; //TODO: show an error on gui
        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
    }

    public void setSelectedItems(List<Item> itemsChosen){
        this.chosenItems = itemsChosen;
    }

    private void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    public void clearTilesList() {
        for (Position items : selectedItems) clearNodeByColumnRow(items.getColumn(), items.getRow());
    }

    //PERSONAL GOAL CARD

    public void setPersonalGoalCard(int id){
        personalGoalCardImageView.setImage(new Image("/gui/myShelfieImages/personal_goal_cards/personal_goal_card_" + id + ".png"));
    }

    //BOOKSHELVES

    public void initializeBookshelves(List<String> nicknames) {
        numberPlayers = nicknames.size();
        this.nicknames = nicknames;
        nicknames.remove(nickname); //TODO: a map between nicknames and bookshelves' imageview
        if (numberPlayers == 2) {
            firstBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            firstBookshelfLabel.setText(nicknames.get(0) + "'s bookshelf");
        } else if (numberPlayers == 3) {
            firstBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            firstBookshelfLabel.setText(nicknames.get(1) + "'s bookshelf");
        } else if (numberPlayers == 4) {
            firstBookshelfImageView.setImage(new Image("/gui/myShelfieImages/boards/bookshelf.png"));
            firstBookshelfLabel.setText(nicknames.get(2) + "'s bookshelf");
        }
    }

    public void updateBookshelf(String owner, Map<Position, Item> bookshelf) {

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
    }
}
