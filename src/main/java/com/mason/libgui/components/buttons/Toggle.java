
package com.mason.libgui.components.buttons;

import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * A Button that switches between an on and off state.
 * @author Adam Whittaker
 */
public class Toggle extends Button{


    /**
     * Whether the button is "on"
     */
    private boolean clicked = false;


    /**
     * Creates an instance
     * @param info
     * @param x
     * @param y
     * @param w
     * @param h
     */
    public Toggle(StyleInfo info, int x, int y, int w, int h){
        super(info, x, y, w, h);
    }
    
    
    @Override
    public void render(Graphics2D g){
        info.RENDER_UTILS.drawButton(g, info, x, y, width, height, isHovering(), clicked);
    }


    /**
     * Updates the clicked value
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e){
        clicked = !clicked;
    }


    /**
     * @return True if the button is on
     */
    public boolean isPressed(){
        return clicked;
    }
    
}
