
package com.mason.libgui.components.dragging;

import com.mason.libgui.core.UIComponent;

import java.awt.*;

/**
 * A component that is draggable.
 * @author Adam Whittaker
 */
public abstract class DraggableComponent extends UIComponent implements Draggable{


    /**
     * Forwarded constructor
     */
    public DraggableComponent(int x, int y, int w, int h){
        super(x, y, w, h);
    }


    @Override
    public void startDrag(){}

    @Override
    public void releaseDrag(){}

    @Override
    public boolean validDragLocation(int x, int y){
        return true;
    }

    @Override
    public boolean withinDragRegion(int mx, int my){
        return true;
    }

    @Override
    public void processInvalidDrag(int mx, int my){}


    public static DraggableComponent getTestInstance(int x, int y, int w, int h){
        return new DraggableComponent(x, y, w, h){

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
