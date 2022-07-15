
package com.mason.libgui.components.draggables;

import com.mason.libgui.components.draggables.Draggable;
import com.mason.libgui.core.UIComponent;
import java.awt.event.MouseEvent;

/**
 *
 * @author Adam Whittaker
 */
public abstract class DraggableComponent extends UIComponent implements Draggable {
    
    
    private int diffX;
    private int diffY;
    private boolean dragging;
    
    
    public DraggableComponent(int x, int y, int w, int h){
        super(x, y, w, h);
    }
    
    
    @Override
    public void mousePressed(MouseEvent e){
        diffX = e.getX() - x;
        diffY = e.getY() - y;
        dragging = true;
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(dragging){
            x = e.getX() - diffX;
            y = e.getY() - diffY;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        dragging = false;
    }
    
}
