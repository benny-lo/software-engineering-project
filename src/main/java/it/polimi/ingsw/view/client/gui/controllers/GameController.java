package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.gui.ItemImageView;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static it.polimi.ingsw.utils.gui.CloseWindow.exitChat;


public class GameController implements Initializable {
    private static GUInterface guInterface;
    final private String CUP= "gui/17_MyShelfie_BGA/item_tiles/Trofei1.1.png";
    final private String CAT= "gui/17_MyShelfie_BGA/item_tiles/Gatti1.1.png";
    final private String BOOK= "gui/17_MyShelfie_BGA/item_tiles/Libri1.1.png";
    final private String PLANT= "gui/17_MyShelfie_BGA/item_tiles/Piante1.1.png";
    final private String GAME= "gui/17_MyShelfie_BGA/item_tiles/Giochi1.1.png";
    final private String FRAME= "gui/17_MyShelfie_BGA/item_tiles/Cornici1.1.png";
    private final List<Position> selectedItems = new ArrayList<>();
    private String currentPlayer;
    private final static int cellSizeLivingRoom = 49;
    @FXML
    private GridPane livingRoomGridPane;
    @FXML
    private Button sendButton;
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private Button bookshelvesButton;
    @FXML
    private Button enterChatButton;

    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }

    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
        currentPlayerLabel.setText("It's " + currentPlayer + "'s turn!");
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
        ItemImageView imageView= new ItemImageView(image, column, row);
        imageView.setOnMouseClicked(mouseEvent -> this.selectItem(new Position(row, column)));
        imageView.setFitWidth(cellSizeLivingRoom);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

    public void selectItem(Position position) {
        if (!guInterface.getNickname().equals(currentPlayer)) return; //TODO: show an error on gui; not your turn
        if (selectedItems.contains(position)) { //TODO: fix this
            selectedItems.remove(position);
            ItemImageView itemImageView = (ItemImageView) livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0);
            itemImageView.setVisible(true);
            return;
        }
        if (selectedItems.size() <= 2) {
            selectedItems.add(position);
            ItemImageView itemImageView = (ItemImageView) livingRoomGridPane.getChildren().stream().filter(n -> GridPane.getColumnIndex(n) == position.getColumn() && GridPane.getRowIndex(n) == position.getRow()).toList().get(0);
            itemImageView.setVisible(false);
            return;
        }
        //TODO: show an error on gui; already 3 tiles selected
    }



    public void selectFromLivingRoom() throws IOException {
        if (selectedItems.size() < 1) return; //TODO: show an error on gui
        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
        System.out.println("hai inviato " + selectedItems.size());
    }

    public void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    private void clearTilesList(List<Position> tiles) {
        for (Position selectedItem : tiles) clearNodeByColumnRow(selectedItem.getColumn(), selectedItem.getRow());
    }

    //CHAT
    public void enterChat() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/Chat.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("MyShelfieChat");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.show();

        stage.setOnCloseRequest(event -> {event.consume();
            exitChat(stage);});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
    }

}
