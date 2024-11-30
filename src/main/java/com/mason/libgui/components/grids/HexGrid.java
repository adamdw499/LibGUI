package com.mason.libgui.components.grids;

import com.mason.libgui.utils.Utils;
import com.mason.libgui.utils.Utils.Unfinished;

import java.util.Iterator;

@Unfinished
public class HexGrid implements Grid<HexTile>{

    @Override
    public int dist(HexTile a, HexTile b){
        return 0;
    }

    @Override
    public void resetTraversal(){

    }

    @Override
    public HexTile getRandomTile(){
        return null;
    }

    @Override
    public Iterable<HexTile> neighbours(HexTile hexTile){
        return null;
    }

    @Override
    public Iterator<HexTile> iterator(){
        return null;
    }

}
