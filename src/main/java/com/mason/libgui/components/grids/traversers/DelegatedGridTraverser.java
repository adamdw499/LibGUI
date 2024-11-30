package com.mason.libgui.components.grids.traversers;

import com.mason.libgui.components.grids.Tile;

public interface DelegatedGridTraverser<T extends Tile<T>> extends GridTraverser<T>{


    void process(T t);

    boolean filter(T t);

    boolean earlyCutoff(T t);

}
