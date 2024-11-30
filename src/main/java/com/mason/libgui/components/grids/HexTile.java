package com.mason.libgui.components.grids;


public class HexTile extends Tile<HexTile>{


    private int q, r;
    private HexTile nWest, nEast, east, sEast, sWest, west;


    public HexTile(){}

    public HexTile(int q, int r, int s){
        setCoords(q, r, s);
    }


    public int getQ(){
        return q;
    }

    public int getR(){
        return r;
    }

    public int getS(){
        return -q - r;
    }

    public final void setCoords(int q, int r, int s){
        if(q + r + s != 0) throw new IllegalArgumentException("Invalid Hex coordinates: (" + q + ", " + r + ", " + s + ")");
        this.q = q;
        this.r = r;
    }

    protected void setNeighbours(HexTile nw, HexTile ne, HexTile e, HexTile se, HexTile sw, HexTile w){
        nWest = nw;
        nEast = ne;
        east = e;
        sEast = se;
        sWest = sw;
        west = w;
    }

    public HexTile getNorthWest(){
        return nWest;
    }

    public HexTile getNorthEast(){
        return nEast;
    }

    public HexTile getWest(){
        return west;
    }

    public HexTile getEast(){
        return east;
    }

    public HexTile getSouthWest(){
        return sWest;
    }

    public HexTile getSouthEast(){
        return sEast;
    }


}
