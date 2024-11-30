package com.mason.libgui.components.grids;

public interface Grid<T extends Tile<T>> extends Iterable<T>{

    int dist(T a, T b);

    void resetTraversal();

    T getRandomTile();

    Iterable<T> neighbours(T t);


}
