
package com.mason.libgui.components.dragging;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * An interface for a component that can be dragged.
 * @author Adam Whittaker
 */
public interface Draggable extends MouseListener, MouseMotionListener{


    /**
     * Tests whether the mouse coordinates are in the "drag region", in which case drag should be activated.
     * @param x relative mouse x
     * @param y relative mouse y
     * @return True if so.
     */
    boolean withinDragRegion(int x, int y);

    /**
     * Checks if the Draggable can be dragged to these coordinates.
     * @param mx parent coordinates
     * @param my parent coordinates
     * @return true if it can
     */
    boolean validDragLocation(int mx, int my);

    /**
     * Reacts to a drag coordinate that is not a valid drag location.
     * @param mx parent coordinates
     * @param my parent coordinates
     */
    void processInvalidDrag(int mx, int my);


    /**
     * Lets the component know that dragging has started.
     */
    void startDrag();

    /**
     * Lets the component know that dragging has ended.
     */
    void releaseDrag();

    /**
     * The signature of the UIComponent method of the same name. All Draggables should be UIComponents anyway, this
     * is just so that the DragManager can use the method.
     */
    void setX(int x);

    /**
     * The signature of the UIComponent method of the same name. All Draggables should be UIComponents anyway, this
     * is just so that the DragManager can use the method.
     */
    void setY(int y);

    /**
     * The signature of the UIComponent method of the same name. All Draggables should be UIComponents anyway, this
     * is just so that the DragManager can use the method.
     */
    int getX();

    /**
     * The signature of the UIComponent method of the same name. All Draggables should be UIComponents anyway, this
     * is just so that the DragManager can use the method.
     */
    int getY();

}
