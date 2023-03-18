package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Grid {
    private Item [][] grid = new Item[9][9];
    public Grid(int numberPlayers, Bag bag) {
        this.grid = grid;

        for (int i=0; i<9; i++){
            grid[0][i] = Item.LOCKED;
            grid[8][i] = Item.LOCKED;
        }
        for(int i=1; i<8; i++){
            grid[i][0] = Item.LOCKED;
            grid[i][8] = Item.LOCKED;
        }
        grid[1][1] = Item.LOCKED;
        grid[1][2] = Item.LOCKED;
        grid[2][1] = Item.LOCKED;
        grid[2][2] = Item.LOCKED;
        grid[3][1] = Item.LOCKED;
        grid[1][5] = Item.LOCKED;
        grid[1][6] = Item.LOCKED;
        grid[1][7] = Item.LOCKED;
        grid[2][6] = Item.LOCKED;
        grid[2][7] = Item.LOCKED;
        grid[6][1] = Item.LOCKED;
        grid[6][2] = Item.LOCKED;
        grid[7][1] = Item.LOCKED;
        grid[7][2] = Item.LOCKED;
        grid[7][3] = Item.LOCKED;
        grid[5][7] = Item.LOCKED;
        grid[6][6] = Item.LOCKED;
        grid[6][7] = Item.LOCKED;
        grid[7][6] = Item.LOCKED;
        grid[7][7] = Item.LOCKED;
        if(numberPlayers>2){
            grid[0][3] = Item.FREE;
            grid[2][2] = Item.FREE;
            grid[2][6] = Item.FREE;
            grid[3][8] = Item.FREE;
            grid[5][0] = Item.FREE;
            grid[6][2] = Item.FREE;
            grid[6][6] = Item.FREE;
            grid[8][5] = Item.FREE;
            if(numberPlayers==4){
                grid[0][4] = Item.FREE;
                grid[1][5] = Item.FREE;
                grid[4][0] = Item.FREE;
                grid[5][1] = Item.FREE;
                grid[4][8] = Item.FREE;
                grid[5][7] = Item.FREE;
                grid[7][3] = Item.FREE;
                grid[8][4] = Item.FREE;
            }
        }
        fillBoard(bag);
    }
    public void printGrid(){
        for(int i=0; i<9; i++) {
            System.out.println(grid[i][0] + " | " + grid[i][1] + " | " + grid[i][2] + " | " + grid[i][3] + " | " + grid[i][4] + " | " + grid[i][5] + " | " + grid[i][6] + " | " + grid[i][7] + " | " + grid[i][8]);
        }
    }
    public void fillBoard(Bag bag){
        this.grid=grid;
        for(int i=0; i<9; i++){
            for(int j=0; j<9; j++){
                if(grid[i][j]==Item.FREE || grid[i][j]==null){
                    grid[i][j]=bag.extract();
                }
            }
        }
    }

    public Item tileAt(int x, int y) {
        return this.grid[x][y];
    }

    public Boolean selectable(int x, int y){
        this.grid = grid;
        if(grid[x][y]==Item.FREE || grid[x][y]==Item.LOCKED){
            return false;
        }else if(x==0){
            if(grid[x][y+1]==Item.FREE || grid[x][y+1]==Item.LOCKED ||
                    grid[x][y-1]==Item.FREE || grid[x][y-1]==Item.LOCKED ||
                    grid[x+1][y]==Item.FREE || grid[x+1][y]==Item.LOCKED){
                return true;
            }else{
                return false;
            }
        } else if(x==8){
            if(grid[x][y+1]==Item.FREE || grid[x][y+1]==Item.LOCKED ||
                    grid[x][y-1]==Item.FREE || grid[x][y-1]==Item.LOCKED ||
                    grid[x-1][y]==Item.FREE || grid[x-1][y]==Item.LOCKED){
                return true;
            }else{
                return false;
            }
        }else if(y==0){
            if(grid[x][y+1]==Item.FREE || grid[x][y+1]==Item.LOCKED ||
                    grid[x+1][y]==Item.FREE || grid[x+1][y]==Item.LOCKED ||
                    grid[x-1][y]==Item.FREE || grid[x-1][y]==Item.LOCKED){
                return true;
            }else{
                return false;
            }
        }else if(y==8){
            if(grid[x][y-1]==Item.FREE || grid[x][y-1]==Item.LOCKED ||
                    grid[x+1][y]==Item.FREE || grid[x+1][y]==Item.LOCKED ||
                    grid[x-1][y]==Item.FREE || grid[x-1][y]==Item.LOCKED){
                return true;
            }else{
                return false;
            }
        }else if(grid[x][y-1]==Item.FREE || grid[x][y-1]==Item.LOCKED ||
                grid[x+1][y]==Item.FREE || grid[x+1][y]==Item.LOCKED ||
                grid[x-1][y]==Item.FREE || grid[x-1][y]==Item.LOCKED ||
                grid[x][y+1]==Item.FREE || grid[x][y+1]==Item.LOCKED){
            return true;
        }else{
            return false;
        }
    }

    public Boolean adjacent(int x1, int y1, int x2, int y2){
        if(x1+y1-x2-y2==1 || x1+y1-x2-y2==-1){
            return true;
        }else{
            return false;
        }
    }

    public Item selectTiles(int x1, int y1) throws TileException{
        this.grid=grid;

        if(selectable(x1, y1)) {
            return tileAt(x1, y1);
        }else {
            throw new TileException("You can't get this tile");
        }
    }

    public ArrayList<Item> selectTiles(int x1, int y1, int x2, int y2) throws TileException{
        if(adjacent(x1,y1,x2,y2)) {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(selectTiles(x1, y1));
            items.add(selectTiles(x2, y2));
            return items;
        }else{
            throw new TileException("Tiles aren't adjacent");
        }
    }

    //consecutive presuppone che t1 e t2 siano adiacenti perchè se no si ferma prima
    public Boolean consecutive(int x1, int y1, int x2, int y2, int x3, int y3){
        if((x1==x2 && x2==x3 && (y3==y2+1 || y3==y2-1 || y3==y1+1 || y3==y1-1)) ||
                (y1==y2 && y2==y3 && (x3==x2+1 || x3==x2-1 || x3==x1+1 || x3==x1-1))){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Item> selectTiles(int x1, int y1, int x2, int y2, int x3, int y3) throws TileException{
        if(adjacent(x1,y1,x2,y2)) {
            ArrayList<Item> items = new ArrayList<Item>();
            items.add(selectTiles(x1, y1));
            items.add(selectTiles(x2, y2));
            if(consecutive(x1,y1,x2,y2,x3,y3)){
                items.add(selectTiles(x3, y3));
                return items;
            }else{
                throw new TileException("The third tile isn't consecutive");
            }
        }else{
            throw new TileException("Tiles aren't adjacent");
        }
    }
}
