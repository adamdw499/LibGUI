
package com.mason.libgui.components;

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
    
    
    protected boolean hovering = false;
    public StyleInfo colorScheme;
    
    
    public Button(StyleInfo col, int x, int y, int w, int h){
        super(x, y, w, h);
        colorScheme = col;
    }
    
    
    @Override
    public void render(Graphics2D g){
        RenderUtils.drawButton(g, colorScheme, x, y, width, height, hovering, false);
    }
    
    @Override
    public void tick(int mx, int my){
        if(hovering && !withinBounds(mx, my)) hovering = false; 
    }
    
    
    @Override
    public void mouseMoved(MouseEvent e){
        if(!hovering) hovering = true;
    }
    
}
