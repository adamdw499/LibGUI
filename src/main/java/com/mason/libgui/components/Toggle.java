
package com.mason.libgui.components;

import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.RenderUtils;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 *
 * @author Adam Whittaker
 */
public class Toggle extends Button{
    
    
    private boolean clicked = false;
    
    
    public Toggle(StyleInfo col, int x, int y, int w, int h){
        super(col, x, y, w, h);
    }
    
    
    @Override
    public void render(Graphics2D g){
        RenderUtils.drawButton(g, colorScheme, x, y, width, height, hovering, clicked);
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e){
        clicked = !clicked;
    }
    
    
    public boolean isPressed(){
        return clicked;
    }
    
}
