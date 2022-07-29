
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
public abstract class Button extends UIComponent{
    
    
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


    @Override
    public abstract void mouseClicked(MouseEvent e);


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
    
}
