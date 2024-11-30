package com.mason.libgui.components.grids;


import java.util.LinkedList;

public class RectTile extends Tile<RectTile>{


    private int x, y;
    private RectTile north, east, south, west;


    public RectTile(){}

    public RectTile(int x, int y){
        this.x = x;
        this.y = y;
    }


    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }


    protected void setNeighbours(RectTile north, RectTile east, RectTile south, RectTile west){
        this.north = north;
        this.east = east;
        this.west = west;
        this.south = south;
    }

    public RectTile getNorth(){
        return north;
    }

    public RectTile getEast(){
        return east;
    }

    public RectTile getSouth(){
        return south;
    }

    public RectTile getWest(){
        return west;
    }

}
