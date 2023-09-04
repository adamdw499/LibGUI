package com.mason.libgui.components.dragging;

import com.mason.libgui.core.UIComponentManager;

import java.awt.*;


/**
 * A draggable component which can't move past the bounds of its parent.
 */
public abstract class PanLockedDraggableComponent extends DraggableComponent{


    /**
     * The dimensions of the parent.
     */
    private int parentWidth, parentHeight;


    /**
     * Forwarded constructor
     */
    public PanLockedDraggableComponent(int x, int y, int w, int h){
        super(x, y, w, h);
    }


    /**
     * Grabs the dimensions of the parent upon being added.
     */
    @Override
    public void setParent(UIComponentManager parent){
        parentWidth = parent.getWidth();
        parentHeight = parent.getHeight();
    }


    /**
     * Checks if the location is within bounds.
     * @param x parent coordinates
     * @param y parent coordinates
     */
    @Override
    public boolean validDragLocation(int x, int y){
        return x>=0 && y>=0 && x+width<=parentWidth && y+height<=parentHeight;
    }

    /**
     * Puts the component back in the parent's bounds.
     * @param mx parent coordinates
     * @param my parent coordinates
     */
    @Override
    public void processInvalidDrag(int mx, int my){
        if(mx < 0) x = 0;
        else if(mx+width > parentWidth) x = parentWidth - width;
        else x = mx;
        if(my < 0) y = 0;
        else if(my+height > parentHeight) y = parentHeight - height;
        else y = my;
    }


    public static PanLockedDraggableComponent getTestInstance(int x, int y, int w, int h){
        return new PanLockedDraggableComponent(x, y, w, h){

            @Override
            public void render(Graphics2D g){
                g.setColor(Color.PINK);
                g.fillRect(x, y, width, height);
            }

            @Override
            public void tick(int mx, int my){}

        };
    }

}
