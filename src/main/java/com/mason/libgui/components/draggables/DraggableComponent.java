
package com.mason.libgui.components.draggables;

import com.mason.libgui.core.UIComponent;

import java.awt.event.MouseEvent;

/**
 * A component that is draggable. Contains the code for moving the component.
 * @author Adam Whittaker
 */
public abstract class DraggableComponent extends UIComponent implements Draggable{


    /**
     * The difference between the mouse click and the top left corner of the component.
     */
    protected int diffX;
    protected int diffY;
    /**
     * Whether the component is currently being dragged.
     */
    protected boolean dragging;


    /**
     * Forwarded constructor
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public DraggableComponent(int x, int y, int w, int h){
        super(x, y, w, h);
    }


    /**
     * Sets the diff and dragging boolean when the mouse is pressed.
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e){
        diffX = e.getX() - x;
        diffY = e.getY() - y;
        dragging = true;
    }

    /**
     * Updates the x and y coords
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e){
        if(dragging){
            x = e.getX() - diffX;
            y = e.getY() - diffY;
        }
    }

    /**
     * Releases the dragging status.
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e){
        dragging = false;
    }
    
}
