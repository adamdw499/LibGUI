package com.mason.libgui.utils;

import java.awt.*;

public class DefaultRenderUtils extends RenderUtils{


    public DefaultRenderUtils(int lineWidth){
        super(lineWidth);
    }


    @Override
    public void drawButton(Graphics2D g, StyleInfo c, int x, int y, int w, int h, boolean highlighted,
                           boolean clicked){
        if(clicked) g.setColor(c.FORE_HIGHLIGHT.brighter());
        else if(highlighted) g.setColor(c.FORE_HIGHLIGHT);
        else g.setColor(c.FOREGROUND);
        g.fillRect(x, y, w, h);
        drawBorder(g, c, x, y, w, h);
    }

    @Override
    public void drawBorder(Graphics2D g, StyleInfo c, int x, int y, int w, int h){
        g.setColor(c.BORDER);
        g.fillRect(x, y, lineWidth, h);
        g.fillRect(x, y, w, lineWidth);
        g.fillRect(x+w- lineWidth, y, lineWidth, h);
        g.fillRect(x, y+h- lineWidth, w, lineWidth);
        g.setColor(c.BORDER.brighter());
        g.fillRect(x+ lineWidth /3, y+ lineWidth /3, lineWidth /3, h-2* lineWidth /3);
        g.fillRect(x+ lineWidth /3, y+ lineWidth /3, w-2* lineWidth /3, lineWidth /3);
        g.fillRect(x+w-2* lineWidth /3, y+ lineWidth /3, lineWidth /3, h-2* lineWidth /3);
        g.fillRect(x+ lineWidth /3, y+h-2* lineWidth /3, w-2* lineWidth /3, lineWidth /3);
    }

    @Override
    public void drawBorderLine(Graphics2D g, StyleInfo c, int x, int y, int len, boolean horizontal){
        g.setColor(c.BORDER);
        if(horizontal) g.fillRect(x, y, len, lineWidth);
        else g.fillRect(x, y, lineWidth, len);
        g.setColor(c.BORDER.brighter());
        if(horizontal){
            g.fillRect(x+ lineWidth /3, y+ lineWidth /3, len-2* lineWidth /3, lineWidth /3);
        }else{
            g.fillRect(x+ lineWidth /3, y+ lineWidth /3, lineWidth /3, len-2* lineWidth /3);
        }
    }


}
