
package com.mason.libgui.components.buttons;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.RenderUtils;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Adam Whittaker
 */
public class Button extends UIComponent{
    
    
    private boolean hovering = false;
    public StyleInfo style;
    
    
    public Button(StyleInfo info, int x, int y, int w, int h){
        super(x, y, w, h);
        style = info;
    }
    
    
    @Override
    public void render(Graphics2D g){
        RenderUtils.drawButton(g, style, x, y, width, height, hovering, false);
    }
    
    @Override
    public void tick(int mx, int my){
        if(hovering && !withinBounds(mx, my)) hovering = false; 
    }

    protected boolean isHovering(){
        return hovering;
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e){
        if(!hovering) hovering = true;
    }
    
}
