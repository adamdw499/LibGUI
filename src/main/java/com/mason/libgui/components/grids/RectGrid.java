package com.mason.libgui.components.grids;


import java.util.Iterator;

import static com.mason.libgui.utils.Utils.R;
import static java.lang.Math.*;

public class RectGrid implements Grid<RectTile>{


    private final int height, width;
    private final RectTile[][] tiles;


    public RectGrid(int width, int height){
        this(new RectTile[height][width]);
    }

    public RectGrid(RectTile[][] tiles){
        this.tiles = tiles;
        height = tiles.length;
        width = tiles[0].length;

        for(int y=0; y<height; y++) for(int x=0; x<width; x++){
            tiles[y][x].setX(x);
            tiles[y][x].setY(y);
        }

        tiles[0][0].setNeighbours(null, tiles[0][1], tiles[1][0], null);
        tiles[height-1][0].setNeighbours(tiles[height-2][0], tiles[height-1][1], null, null);
        tiles[0][width-1].setNeighbours(null, null, tiles[1][width-1], tiles[0][width-2]);
        tiles[height-1][width-1].setNeighbours(tiles[height-2][width-1], null, null, tiles[height-1][width-2]);

        for(int x=1; x<width-1; x++){
            tiles[0][x].setNeighbours(null, tiles[0][x+1], tiles[1][x-1], tiles[0][x-1]);
            tiles[height-1][x].setNeighbours(tiles[height-2][x], tiles[height-1][x+1], null, tiles[height-1][x-1]);
        }

        for(int y=1; y<height-1; y++){
            tiles[y][0].setNeighbours(tiles[y-1][0], tiles[y][1], tiles[y+1][0], null);
            tiles[y][width-1].setNeighbours(tiles[y-1][width-1], null, tiles[y+1][width-1], tiles[y][width-2]);
        }

        for(int y = 1; y<height-1; y++) for(int x = 1; x<width-1; x++){
            tiles[y][x].setNeighbours(tiles[y-1][x], tiles[y][x+1], tiles[y+1][x], tiles[y][x-1]);
        }
    }


    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public RectTile[][] getTiles(){
        return tiles;
    }

    public RectTile getTile(int x, int y){
        return tiles[y][x];
    }

    @Override
    public int dist(RectTile a, RectTile b){
        return abs(a.getX()-b.getX()) + abs(a.getY()-b.getY());
    }

    @Override
    public void resetTraversal(){
        for(RectTile tile : this){
            tile.resetTraversal();
        }
    }

    @Override
    public RectTile getRandomTile(){
        return tiles[R.nextInt(height)][R.nextInt(width)];
    }

    @Override
    public Iterable<RectTile> neighbours(RectTile t){
        return () -> new Iterator<>(){


            final int maxX = min(t.getX()+1, width-1),
                    maxY = min(t.getY()+1, height-1),
                    minX = max(t.getX()-1, 0);
            int x = minX-1, y = max(t.getY()-1, 0);


            @Override
            public boolean hasNext(){
                return y <= maxY;
            }

            @Override
            public RectTile next(){
                x++;
                if(x > maxX){
                    x = minX;
                    y++;
                }
                if(x == t.getX() && y == t.getY()) x++;
                return tiles[y][x];
            }
        };
    }

    public Iterable<RectTile> row(int y){
        return () -> new Iterator<>(){

            int x = -1;

            @Override
            public boolean hasNext(){
                return x<width-1;
            }

            @Override
            public RectTile next(){
                x++;
                return tiles[y][x];
            }

        };
    }

    public Iterable<RectTile> col(int x){
        return () -> new Iterator<>(){

            int y = -1;

            @Override
            public boolean hasNext(){
                return y<height-1;
            }

            @Override
            public RectTile next(){
                y++;
                return tiles[y][x];
            }

        };
    }

    @Override
    public Iterator<RectTile> iterator(){
        return new Iterator<>(){


            int x=-1, y=0;


            @Override
            public boolean hasNext(){
                return y < height;
            }

            @Override
            public RectTile next(){
                x++;
                if(x == width){
                    x = 0;
                    y++;
                }
                return tiles[y][x];
            }
        };
    }

}
