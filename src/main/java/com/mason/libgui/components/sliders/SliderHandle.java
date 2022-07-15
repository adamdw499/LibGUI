
package com.mason.libgui.components.sliders;

import com.mason.libgui.core.UIComponent;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author Adam Whittaker
 */
public abstract class SliderHandle extends UIComponent{


    private int diff;
    private boolean dragging = false;


    public SliderHandle(int x, int y, int w, int h){
        super(x, y, w, h);
    }


    protected void mousePressed(MouseEvent e, boolean horizontal){
        dragging = true;
        if(horizontal) diff = e.getX() - x;
        else diff = e.getY() - y;
    }

    protected void mouseDragged(MouseEvent e, boolean horizontal, int min, int max){
        if(dragging){
            if(horizontal){
                x = e.getX() - diff;
                if(x > max) x = max;
                else if(x < min) x = min;
            }else{
                y = e.getY() - diff;
                if(y > max) y = max;
                else if(y < min) y = min;
            }
        }
    }
    
    protected void stopDragging(){
        dragging = false;
    }
    
    protected boolean isDragging(){
        return dragging;
    }
    
    protected void mouseWheelMoved(MouseWheelEvent e, boolean horizontal, int min, int max){
        if(horizontal){
            x += e.getWheelRotation()*width/2;
            if(x > max) x = max;
            else if(x < min) x = min;
        }else{
            y += e.getWheelRotation()*height/2;
            if(y > max) y = max;
            else if(y < min) y = min;
        }
    }

    protected double value(double val, double min, double max){
        return (val - min)/(max - min);
    }
    
    
    @Override
    public abstract void render(Graphics2D g);

}
