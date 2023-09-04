
package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponent;

import java.awt.*;

/**
 * An invisible component that blocks any clicks in its boundaries.
 * @author Adam Whittaker
 */
public class ClickBlocker extends UIComponent{
    
    
    public ClickBlocker(int x, int y, int w, int h){
        super(x, y, w, h);
    }

    @Override
    public void render(Graphics2D g){}

    @Override
    public void tick(int mx, int my){}

}
