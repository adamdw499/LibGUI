
package com.mason.libgui.components.draggables;

import java.awt.event.MouseEvent;

/**
 *
 * @author Adam Whittaker
 */
public abstract class SnapDraggable extends DraggableComponent {
    
    
    private int[][] snapGrid;
    
    
    public SnapDraggable(int[][] grid, int x, int y, int width, int height){
        super(x, y, width, height);
        snapGrid = grid;
        snap();
    }
    
    
    private void snap(){
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
    
    //distance squared
    private double dist(int[] c){
        return (c[0]-x)*(c[0]-x) + (c[1]-y)*(c[1]-y);
    }
    
    
    @Override
    public void mouseReleased(MouseEvent e){
        super.mouseReleased(e);
        snap();
    }
    
}
