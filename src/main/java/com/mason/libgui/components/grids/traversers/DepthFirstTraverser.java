package com.mason.libgui.components.grids.traversers;

import com.mason.libgui.components.grids.Grid;
import com.mason.libgui.components.grids.Tile;

public abstract class DepthFirstTraverser<T extends Tile<T>> implements DelegatedGridTraverser<T>{


    @Override
    public void traverse(Grid<T> grid, T start){
        recur(grid, start);
    }


    private boolean recur(Grid<T> grid, T current){
        process(current);
        current.traverse();
        if(earlyCutoff(current)) return true;

        for(T t : grid.neighbours(current)) if(filter(t)){
            if(recur(grid, t)) return true;
            backtrack(t);
        }
        return false;
    }


    protected abstract void backtrack(T t);

}
