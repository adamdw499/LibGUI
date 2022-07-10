
package com.mason.libgui.components;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.RenderUtils;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static com.mason.libgui.utils.StyleInfo.DEFAULT_STYLE_INFO;


/**
 *
 * @author Adam Whittaker
 */
public abstract class SmoothSlider extends UIComponent implements Draggable{
    
    
    protected final boolean horizontal;
    protected final SliderHandle handle;
    
    
    public SmoothSlider(int x, int y, int w, int h, SliderHandle handle, boolean horizontal){
        super(x, y, w, h);
        this.horizontal = horizontal;
        this.handle = handle;
        handle.setX(x + w/2 - handle.getWidth()/2);
        handle.setY(y + h/2 - handle.getHeight()/2);
    }
    
    
    public double value(){
        return handle.value(horizontal ? handle.getX() : handle.getY(), min(), max());
    }
    
    private int min(){
        return horizontal ? x : y;
    }
    
    private int max(){
        return horizontal ? x+width-handle.getWidth() : y+height-handle.getHeight();
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e){
        if(horizontal){
            handle.setX(e.getX() - handle.getWidth()/2);
            if(handle.getX() > max()) handle.setX(max());
            else if(handle.getX() < min()) handle.setX(min());
        }else{
            handle.setY(e.getY() - handle.getHeight()/2);
            if(handle.getY() > max()) handle.setY(max());
            else if(handle.getY() < min()) handle.setY(min());
        }
    }
        
    @Override
    public void mousePressed(MouseEvent e){
        if(handle.withinBounds(e.getX(), e.getY())) handle.mousePressed(e, horizontal);
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(handle.isDragging()) handle.mouseDragged(e, horizontal, min(), max());
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        handle.stopDragging();
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        handle.mouseWheelMoved(e, horizontal, min(), max());
    }
    
    
    @Override
    public abstract void render(Graphics2D g);
    
    
    public static SmoothSlider getDefaultSlider(int x, int y, int len, boolean horizontal){
        SliderHandle handle = new SliderHandle(0, 0, horizontal?40:20, horizontal?20:40){
            
            @Override
            public void render(Graphics2D g){
                RenderUtils.drawSliderHandle(g, DEFAULT_STYLE_INFO, x, y, width, height, horizontal);
            }
            
        };
        
        return new SmoothSlider(x, y, horizontal?len:26, horizontal?26:len, handle, horizontal){
        
            @Override
            public void render(Graphics2D g){
                RenderUtils.drawSlider(g, DEFAULT_STYLE_INFO, x, y, width, height, this.horizontal, handle);
            }
            
        };
    }
    
}
