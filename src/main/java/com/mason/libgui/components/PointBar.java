
package com.mason.libgui.components;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.RenderUtils;
import java.awt.Color;
import java.awt.Graphics2D;

import static com.mason.libgui.utils.RenderUtils.LINE_WIDTH;

/**
 *
 * @author Adam Whittaker
 */
public class PointBar extends UIComponent{
    
    
    private Color color;
    private StyleInfo colorScheme;
    private double current, max;
    
    
    public PointBar(StyleInfo colScheme, Color col, int cur, int _max, int x, int y, int w, int h){
        super(x, y, w, h);
        color = col;
        colorScheme = colScheme;
        current = cur;
        max = _max;
    }
    
    
    public void setColor(Color col){
        color = col;
    }
    
    public void setPoints(double pts){
        current = pts;
    }
    
    public void setMax(double m){
        max = m;
    }
    
    
    @Override
    public void render(Graphics2D g){
        RenderUtils.drawButton(g, colorScheme, x, y, width, height, false, false);
        g.setColor(color);
        g.fillRect(x+LINE_WIDTH, y+LINE_WIDTH, (int)((width-2*LINE_WIDTH)*(current/max)), height-2*LINE_WIDTH);
    }
    
}
