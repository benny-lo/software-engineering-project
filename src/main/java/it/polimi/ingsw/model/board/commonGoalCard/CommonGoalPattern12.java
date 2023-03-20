package it.polimi.ingsw.model.board.commonGoalCard;

import it.polimi.ingsw.model.player.Bookshelf;

public class CommonGoalPattern12 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counter=new int[5];
        for(int i=0; i<5; i++){
            for(int j=0; j<6; j++){
                if(bookshelf.tileAt(i,j)!=null){
                    counter[i]++;
                }else{
                    j=6;
                }
            }
        }
//        if you want to exclude 01234 or 43210
//        if(counter[0]==0 || counter[4]==0) return false;
        int i=0;
        if(counter[i]>counter[i+1]) {
            for(i=0; i<4; i++){
                if(counter[i]-counter[i+1]!=1) return false;
            }
            return true;
        }else if(counter[i+1]>counter[i]){
            for(i=0; i<4; i++){
                if(counter[i+1]-counter[i]!=1) return false;
            }
            return true;
        }else return false;
    }
}
