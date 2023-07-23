
package com.mason.libgui.components.buttons;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A clickable UIComponent.
 * @author Adam Whittaker
 */
public abstract class Button extends UIComponent{


    /**
     * hovering: Whether the mouse is currently over the button.
     */
    private boolean hovering = false;
    public StyleInfo info;
    
    
    public Button(StyleInfo info, int x, int y, int w, int h){
        super(x, y, w, h);
        this.info = info;
    }

    public Button(int x, int y, int w, int h){
        this(StyleInfo.DEFAULT_STYLE_INFO, x, y, w, h);
    }
    
    
    @Override
    public void render(Graphics2D g){
        info.RENDER_UTILS.drawButton(g, info, x, y, width, height, hovering, false);
    }

    /**
     * Sets hovering to false if the mouse is off the button.
     * @param mx
     * @param my
     */
    @Override
    public void tick(int mx, int my){
        if(hovering && !withinBounds(mx, my)) stopHovering();
    }


    /**
     * Sets hovering to true if the mouse is over the button.
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e){
        if(!hovering) startHovering();
    }

    /**
     * Sets hovering parameter to true.
     */
    protected void startHovering(){
        hovering = true;
    }

    /**
     * Sets hovering parameter to false.
     */
    protected void stopHovering(){
        hovering = false;
    }

    protected boolean isHovering(){
        return hovering;
    }


    @Override
    public abstract void mouseClicked(MouseEvent e);


    /**
     * Creates a do-nothing button, for testing purposes.
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public static Button getBlankButton(StyleInfo info, int x, int y, int w, int h){
        return new Button(info, x, y, w, h){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

    public static Button getBlankButton(int x, int y, int w, int h){
        return new Button(x, y, w, h){

            @Override
            public void mouseClicked(MouseEvent e){}

        };
    }

    /**
     * @return A button that does not exist.
     */
    public static Button getNullButton(){
        return new Button(-1, -1, -1, -1){
            @Override
            public void mouseClicked(MouseEvent e){}

            @Override
            public void tick(int mx, int my){}

            @Override
            public void render(Graphics2D g){}

            @Override
            public boolean withinBounds(int mx, int my){
                return false;
            }

        };
    }
    
}
