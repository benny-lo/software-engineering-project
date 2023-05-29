package it.polimi.ingsw.view.client.gui.controllers;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.view.client.gui.GUInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    final private String CUP= "gui/17_MyShelfie_BGA/item_tiles/Trofei1.1.png";
    final private String CAT= "gui/17_MyShelfie_BGA/item_tiles/Gatti1.1.png";
    final private String BOOK= "gui/17_MyShelfie_BGA/item_tiles/Libri1.1.png";
    final private String PLANT= "gui/17_MyShelfie_BGA/item_tiles/Piante1.1.png";
    final private String GAME= "gui/17_MyShelfie_BGA/item_tiles/Giochi1.1.png";
    final private String FRAME= "gui/17_MyShelfie_BGA/item_tiles/Cornici1.1.png";
    private final Image cup = new Image(CUP);
    private final Image cat = new Image(CAT);
    private final Image book = new Image(BOOK);
    private final Image plant = new Image(PLANT);
    private final Image game = new Image(GAME);
    private final Image frame = new Image(FRAME);
    private static GUInterface guInterface;

    @FXML
    private GridPane livingroomGridPane;
    public static void startGameController(GUInterface guInterface){
        GameController.guInterface=guInterface;
    }
    public void setLivingroomGridPane(Item[][] livingroom){
        System.out.println("SetLivingroom");
        if(livingroom==null) return;
        for(int i=livingroom.length-1; i>=0; i--){
            for(int j=0; j<livingroom.length; j++){
                setTile(livingroom[i][j], i, j);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        guInterface.receiveController(this);
    }

    public void setTile(Item item, int column, int row){
        if(item==null) return;
        System.out.println(item + " " + column + " " + row);
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
}
