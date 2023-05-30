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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static it.polimi.ingsw.view.client.gui.controllers.Utils.logout;


public class GameController implements Initializable {
    private static GUInterface guInterface;
    final private String CUP= "gui/17_MyShelfie_BGA/item_tiles/Trofei1.1.png";
    final private String CAT= "gui/17_MyShelfie_BGA/item_tiles/Gatti1.1.png";
    final private String BOOK= "gui/17_MyShelfie_BGA/item_tiles/Libri1.1.png";
    final private String PLANT= "gui/17_MyShelfie_BGA/item_tiles/Piante1.1.png";
    final private String GAME= "gui/17_MyShelfie_BGA/item_tiles/Giochi1.1.png";
    final private String FRAME= "gui/17_MyShelfie_BGA/item_tiles/Cornici1.1.png";
    private List<Position> selectedItems=new ArrayList<>();
    private String currentPlayer;
    private final int cellSizeLivingroom = 50;
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

    //TODO: grid buttons dont work
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

//    public void setLivingRoomGridPane(Item[][] livingRoom){
//        for (int row = 0; row < livingRoom.length; row++) {
//            for (int col = 0; col < livingRoom.length; col++) {
//                Label cell = new Label("(" + row + ", " + col + ")");
//                cell.setStyle("-fx-border-color: black;");
//                cell.setPrefSize(cellSize, cellSize);
//
//                final int rowIndex = row;
//                final int colIndex = col;
//                cell.setOnMouseClicked(event -> System.out.println("Clicked cell at (" + rowIndex + ", " + colIndex + ")"));
//
//                livingRoomGridPane.add(cell, col, row);
//                livingRoomGridPane.setGridLinesVisible(true);
//            }
//        }
//    }

    private void setTile(Item item, int column, int row){
        if(item==null) return;
        String image;
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
        imageView.setFitWidth(cellSizeLivingroom);
        imageView.setPreserveRatio(true);
        livingRoomGridPane.add(imageView, column, row);
    }

//    public void selectItems(ActionEvent event) throws IOException {
//        if (!guInterface.getNickname().equals(currentPlayer)) return;
//        Position position = new Position((int) round(selectButton.getWidth()),(int) round(selectButton.getHeight()));
//        System.out.println("stai cliccando" + position);
//        selectedItems.add(position);
//    }

    public void selectItems(MouseEvent mouseEvent) {
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
                                            logout(stage);});
    }

    //BOOKSHELVES
    public void openBookshelves() {

    }
    public void selectFromLivingRoom() throws IOException{
        if (selectedItems.size() < 1 || selectedItems.size() > 3) return;
        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
    }

    public void clearNodeByColumnRow(int column, int row){
        livingRoomGridPane.getChildren().removeIf( node -> GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row);
    }

    private void clearTilesList(List<Position> tiles) {
        for (Position selectedItem : tiles) clearNodeByColumnRow(selectedItem.getColumn(), selectedItem.getRow());
    }
}
