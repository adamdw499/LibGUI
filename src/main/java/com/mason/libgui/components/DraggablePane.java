
package com.mason.libgui.components;

import com.mason.libgui.core.UIComponent;
import java.awt.event.MouseEvent;

import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;

/**
 *
 * @author Adam Whittaker
 */
public class DraggablePane extends Pane implements Draggable{
    
    
    private final DragRegion dragRegion;
    private int diffX, diffY;
    
    
    public DraggablePane(int x, int y, int w, int h){
        super(x, y, w, h);
        dragRegion = new DragRegion(w, h);
        addComponent(dragRegion);
    }
    
    
    @Override
    public void mousePressed(MouseEvent e){
        if(dragRegion.withinBounds(e.getX()-x, e.getY()-y)) dragRegion.mousePressed(e);
        else super.mousePressed(e);
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        if(dragRegion.isDragging()) dragRegion.mouseDragged(e);
        else super.mouseDragged(e);
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        if(dragRegion.isDragging()) dragRegion.stopDragging();
        else super.mouseReleased(e);
    }
    
    
    protected class DragRegion extends UIComponent{
        
        
        private boolean dragging = false;
        
        
        DragRegion(int w, int h){
            super(0, 0, w, h);
        }
        
        
        @Override
        public boolean withinBounds(int mx, int my){
            return (x<mx && mx<x+LINE_WIDTH && y<my && my<y+height) ||
                    (mx<width+x && x+width-LINE_WIDTH<mx && y<my && my<y+height) || 
                    (x<mx && mx<x+width && y<my && my<y+LINE_WIDTH) ||
                    (x<mx && mx<x+width && y+height-LINE_WIDTH<my && my<y+height);
        }
        
        
        @Override
        public void mousePressed(MouseEvent e){
            dragging = true;
            diffX = e.getX() - DraggablePane.this.x;
            diffY = e.getY() - DraggablePane.this.y;
        }

        @Override
        public void mouseDragged(MouseEvent e){
            if(dragging){
                DraggablePane.this.x = e.getX() - diffX;
                DraggablePane.this.y = e.getY() - diffY;
            }
        }
        
        
        boolean isDragging(){
            return dragging;
        }
        
        void stopDragging(){
            dragging = false;
        }
        
    }
    
}
