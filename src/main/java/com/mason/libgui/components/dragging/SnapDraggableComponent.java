
package com.mason.libgui.components.dragging;


/**
 * A Draggable component that can only be dragged to certain places.
 * @author Adam Whittaker
 */
public abstract class SnapDraggableComponent extends DraggableComponent{


    /**
     * The array of pixel {x, y} coordinates that are acceptable for the component to be in.
     */
    private final int[][] snapGrid;


    /**
     * Simply sets the parameters.
     * @param grid
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public SnapDraggableComponent(int[][] grid, int x, int y, int width, int height){
        super(x, y, width, height);
        snapGrid = grid;
        releaseDrag();
    }
    
    /**
     * Euclidean distance of the point c to the top left corner squared.
     */
    private double dist(int[] c){
        return (c[0]-x)*(c[0]-x) + (c[1]-y)*(c[1]-y);
    }


    /**
     * Moves the object to the point in the snapGrid closest to where it currently is.
     */
    @Override
    public void releaseDrag(){
        int minIndex = -1;
        double minDist = Double.MAX_VALUE, dist;
        for(int n=0; n<snapGrid.length; n++){
            dist = dist(snapGrid[n]);
            if(dist < minDist){
                minDist = dist;
                minIndex = n;
            }
        }
        x = snapGrid[minIndex][0];
        y = snapGrid[minIndex][1];
    }
    
}
