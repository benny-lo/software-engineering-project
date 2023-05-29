package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utils.message.client.LivingRoomSelection;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class GameController implements Initializable {
    private static GUInterface guInterface;
    final private String CUP= "gui/17_MyShelfie_BGA/item_tiles/Trofei1.1.png";
    final private String CAT= "gui/17_MyShelfie_BGA/item_tiles/Gatti1.1.png";
    final private String BOOK= "gui/17_MyShelfie_BGA/item_tiles/Libri1.1.png";
    final private String PLANT= "gui/17_MyShelfie_BGA/item_tiles/Piante1.1.png";
    final private String GAME= "gui/17_MyShelfie_BGA/item_tiles/Giochi1.1.png";
    final private String FRAME= "gui/17_MyShelfie_BGA/item_tiles/Cornici1.1.png";
    private List<Position> selectedItems;
    private String currentPlayer;

    @FXML
    private GridPane livingroomGridPane;
    @FXML
    private Button sendButton;
    @FXML
    private Button selectButton;

    //TODO: grid buttons dont work
    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }
    public void setLivingroomGridPane(Item[][] livingroom){
        if(livingroom==null) return;
        for(int i=0; i<livingroom.length; i++){
            for(int j=0; j<livingroom.length; j++){
                setTile(livingroom[i][j], j, i);
            }
        }
        GridPane.setFillWidth(selectButton, true);
        GridPane.setFillHeight(selectButton, true);
        livingroomGridPane.getChildren().addAll(selectButton);
    }

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
        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);
        livingroomGridPane.add(imageView, column, row);
    }
    public void setCurrentPlayer(String currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public void selectItems() throws IOException {
        if (!guInterface.getNickname().equals(currentPlayer)) return;
        //Position position = new Position((int) round(selectButton.getWidth()),(int) round(selectButton.getHeight()));
        //System.out.println("stai cliccando" + position);
        //selectedItems.add(position);
    }
    public void selectFromLivingRoom() throws IOException{
        if (selectedItems.size() < 1 || selectedItems.size() > 3) return;
        guInterface.selectFromLivingRoom(new LivingRoomSelection(selectedItems));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
    }
}
