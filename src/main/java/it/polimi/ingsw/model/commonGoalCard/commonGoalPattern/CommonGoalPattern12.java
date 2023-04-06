package it.polimi.ingsw.model.commonGoalCard.commonGoalPattern;

import it.polimi.ingsw.model.player.Bookshelf;

/**
 * Class representing the following pattern:
 * five columns of increasing or decreasing height. starting from the
 * first column on the left or on the right, each next column
 * must be made of exactly one more tile. tiles can be of any type
 */

public class CommonGoalPattern12 implements CommonGoalPatternInterface{
    @Override
    public boolean check(Bookshelf bookshelf) {
        int[] counter=new int[bookshelf.getColumns()];
        for(int i=0; i< bookshelf.getColumns(); i++){
            for(int j=0; j< bookshelf.getRows(); j++){
                if(bookshelf.tileAt(j,i)!=null){
                    counter[i]++;
                }else{
                    j=bookshelf.getRows();
                }
            }
        }
//        if you want to include 01234 or 43210, comment the next line
        if(counter[0]==0 || counter[bookshelf.getColumns()-1]==0) return false;

        int i=0;
        if(counter[i]>counter[i+1]) {
            for(i=0; i<bookshelf.getColumns()-1; i++){
                if(counter[i]-counter[i+1]!=1) return false;
            }
            return true;
        }else if(counter[i+1]>counter[i]){
            for(i=0; i<bookshelf.getColumns()-1; i++){
                if(counter[i+1]-counter[i]!=1) return false;
            }
            return true;
        }else return false;
    }
}
