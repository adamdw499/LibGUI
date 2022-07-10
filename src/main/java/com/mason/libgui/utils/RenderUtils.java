
package com.mason.libgui.utils;

import com.mason.libgui.components.SliderHandle;
import java.awt.Graphics2D;

/**
 *
 * @author Adam Whittaker
 */
public class RenderUtils{
    
    
    private RenderUtils(){}
    
    
    public static final int LINE_WIDTH = 6;
    
    
    public static void drawBorder(Graphics2D g, StyleInfo c, int x, int y, int w, int h){
        g.setColor(c.BORDER);
        g.fillRect(x, y, LINE_WIDTH, h);
        g.fillRect(x, y, w, LINE_WIDTH);
        g.fillRect(x+w-LINE_WIDTH, y, LINE_WIDTH, h);
        g.fillRect(x, y+h-LINE_WIDTH, w, LINE_WIDTH);
        g.setColor(c.BORDER.brighter());
        g.fillRect(x+LINE_WIDTH/3, y+LINE_WIDTH/3, LINE_WIDTH/3, h-2*LINE_WIDTH/3);
        g.fillRect(x+LINE_WIDTH/3, y+LINE_WIDTH/3, w-2*LINE_WIDTH/3, LINE_WIDTH/3);
        g.fillRect(x+w-2*LINE_WIDTH/3, y+LINE_WIDTH/3, LINE_WIDTH/3, h-2*LINE_WIDTH/3);
        g.fillRect(x+LINE_WIDTH/3, y+h-2*LINE_WIDTH/3, w-2*LINE_WIDTH/3, LINE_WIDTH/3);
    }
    
    public static void drawButton(Graphics2D g, StyleInfo c, int x, int y, int w, int h, boolean highlighted, 
            boolean clicked){
        if(clicked) g.setColor(c.FORE_HIGHLIGHT.brighter());
        else if(highlighted) g.setColor(c.FORE_HIGHLIGHT);
        else g.setColor(c.FOREGROUND);
        g.fillRect(x, y, w, h);
        drawBorder(g, c, x, y, w, h);
    }
    
    public static void drawBorderLine(Graphics2D g, StyleInfo c, int x, int y, int len, boolean horizontal){
        g.setColor(c.BORDER);
        if(horizontal) g.fillRect(x, y, len, LINE_WIDTH);
        else g.fillRect(x, y, LINE_WIDTH, len);
        g.setColor(c.BORDER.brighter());
        if(horizontal){
            g.fillRect(x+LINE_WIDTH/3, y+LINE_WIDTH/3, len-2*LINE_WIDTH/3, LINE_WIDTH/3);
        }else{
            g.fillRect(x+LINE_WIDTH/3, y+LINE_WIDTH/3, LINE_WIDTH/3, len-2*LINE_WIDTH/3);
        }
    }
    
    public static void drawSliderHandle(Graphics2D g, StyleInfo c, int x, int y, int w, int h, boolean horizontal){
        g.setColor(c.BORDER.brighter());
        g.fillRect(x, y, w, h);
        g.setColor(c.BORDER);
        g.fillRect(x+2, y+2, w-4, h-4);
        g.setColor(c.BORDER.brighter());
        if(horizontal){
            g.drawLine(x+w/4, y+6, x+w/4, y+h-6);
            g.drawLine(x+w/2, y+6, x+w/2, y+h-6);
            g.drawLine(x+3*w/4, y+6, x+3*w/4, y+h-6);
            
            g.drawLine(x+w/4+1, y+6, x+w/4+1, y+h-6);
            g.drawLine(x+w/2+1, y+6, x+w/2+1, y+h-6);
            g.drawLine(x+3*w/4-1, y+6, x+3*w/4-1, y+h-6);
        }else{
            g.drawLine(x+6, y+h/4, x+w-6, y+h/4);
            g.drawLine(x+6, y+h/2, x+w-6, y+h/2);
            g.drawLine(x+6, y+3*h/4, x+w-6, y+3*h/4);
            
            g.drawLine(x+6, y+h/4+1, x+w-6, y+h/4+1);
            g.drawLine(x+6, y+h/2+1, x+w-6, y+h/2+1);
            g.drawLine(x+6, y+3*h/4-1, x+w-6, y+3*h/4-1);
        }
    }
    
    public static void drawSlider(Graphics2D g, StyleInfo col, int x, int y, int width, int height, boolean horizontal, 
            SliderHandle handle){
        if(horizontal){
            drawBorderLine(g, col, x, y, height, false);
            drawBorderLine(g, col, x+width-LINE_WIDTH, y, height, false);
            drawBorderLine(g, col, x, y+height/2-LINE_WIDTH/2, width, true);
        }else{
            drawBorderLine(g, col, x, y, width, true);
            drawBorderLine(g, col, x, y+height-LINE_WIDTH, width, true);
            drawBorderLine(g, col, x+width/2-LINE_WIDTH/2, y, height, false);
        }
        handle.render(g);
    }
    
}
