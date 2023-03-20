package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern9 implements CommonGoalPatternInterface {
    @Override
    public boolean check(Bookshelf bookshelf) {
        Item tile;
        int[] counter=new int[6];
        for(int i=0; i<6; i++){
            for(int j=0; j<5; j++){
                tile=bookshelf.tileAt(i,j);
                if(tile==Item.CAT) counter[0]++;
                if(tile==Item.BOOK) counter[1]++;
                if(tile==Item.CUP) counter[2]++;
                if(tile==Item.FRAME) counter[3]++;
                if(tile==Item.PLANT) counter[4]++;
                if(tile==Item.GAME) counter[5]++;
            }
        }
        for(int i=0; i<6; i++){
            if(counter[i]>=8) return true;
        }
        return false;
    }
}
