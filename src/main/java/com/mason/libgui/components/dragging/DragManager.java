package com.mason.libgui.components.dragging;

import java.awt.event.MouseEvent;

/**
 * Handles dragging components about in a UIComponentManager
 */
public class DragManager{


    /**
     * component: The thing currently being dragged.
     * dragging: Whether it is currently being dragged.
     * diffX, diffY: The relative coordinates of the mouse to the top left corner of the component.
     */
    protected Draggable component;
    protected boolean dragging = false;
    protected int diffX, diffY;


    /**
     * Moves the component.
     */
    public void mouseDragged(MouseEvent e){
        if(component.validDragLocation(e.getX() - diffX, e.getY() - diffY)){
            component.setX(e.getX() - diffX);
            component.setY(e.getY() - diffY);
            component.mouseDragged(e);
        }else{
            component.processInvalidDrag(e.getX() - diffX, e.getY() - diffY);
        }
    }

    /**
     * Calculates relative coordinates, initialises drag and notifies the component that it is being dragged.
     * @param mx absolute coordinates
     * @param my absolute coordinates
     */
    public void startDrag(Draggable comp, int mx, int my){
        diffX = mx - comp.getX();
        diffY = my - comp.getY();
        dragging = true;
        component = comp;
        component.startDrag();
    }

    /**
     * @return Whether something is being dragged currently.
     */
    public boolean isDragging(){
        return dragging;
    }

    /**
     * Shuts off drag and notifies the component that it is being released.
     */
    public void releaseDrag(){
        dragging = false;
        component.releaseDrag();
    }

}
