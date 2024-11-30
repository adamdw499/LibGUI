package com.mason.libgui.components.grids;

public abstract class Tile<T extends Tile<T>>{


    protected int traversalCounter = 0;
    protected T prevTile = null;
    protected boolean traversed;



    public int getTraversalCounter(){
        return traversalCounter;
    }

    public void setTraversalCounter(int traversalCounter){
        this.traversalCounter = traversalCounter;
    }

    public T getPreviousTile(){
        return prevTile;
    }

    public boolean isTraversed(){
        return traversed;
    }

    public void traverse(){
        traversed = true;
    }

    public void backtrack(){
        traversed = false;
    }

    public void setPreviousTile(T t){
        prevTile = t;
    }

    public void resetTraversal(){
        setTraversalCounter(0);
        setPreviousTile(null);
    }

}
