package com.mason.libgui.components.grids.traversers;

import com.mason.libgui.components.grids.Grid;
import com.mason.libgui.components.grids.Tile;

public interface GridTraverser<T extends Tile<T>>{

    void traverse(Grid<T> grid, T start);

}
