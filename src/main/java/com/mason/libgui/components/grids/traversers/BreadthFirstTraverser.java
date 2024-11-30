package com.mason.libgui.components.grids.traversers;

import com.mason.libgui.components.grids.Grid;
import com.mason.libgui.components.grids.Tile;

import java.util.LinkedList;
import java.util.List;

public abstract class BreadthFirstTraverser<T extends Tile<T>> implements DelegatedGridTraverser<T>{


    @Override
    public void traverse(Grid<T> grid, T start){
        List<T> frontier = new LinkedList<>();
        frontier.add(start);
        T current;

        while(!frontier.isEmpty()){
            current = frontier.remove(0);
            process(current);
            if(earlyCutoff(current)) return;

            for(T t : grid.neighbours(current)) if(filter(t)){
                frontier.add(t);
            }
        }
    }

}
