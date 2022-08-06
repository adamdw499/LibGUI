
package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.core.Window;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Adam Whittaker
 */
public class LoadingCircle extends UIComponent{
    
    
    private final Color color;    
    private final double angularVelocity;    
    private final AffineTransform rotation = new AffineTransform();
    private final Rectangle rectangle;
    private final int lineWidth;
    
    
    public LoadingCircle(Color col, int x, int y, int diam, double angularVel, int gapHeight, int lineWidth){
        super(x, y, diam, diam);
        color = col;
        angularVelocity = angularVel;
        rectangle = new Rectangle(x, y+diam/2-gapHeight/2, diam, gapHeight);
        this.lineWidth = lineWidth;
    }

    
    @Override
    public void render(Graphics2D g){
        g.setColor(color);
        g.fillOval(x, y, width, height);
        g.setColor(Window.BACKGROUND_COLOR);
        g.fillOval(x + lineWidth, y + lineWidth, width-2 * lineWidth, height-2 * lineWidth);
        g.fill(rotation.createTransformedShape(rectangle));
    }
    
    @Override
    public void tick(int mx, int my){
        rotation.rotate(angularVelocity, x + width/2D, y + height/2D);
    }


    @Override
    public void setX(int _x){
        rectangle.translate(_x-x, 0);
        super.setX(_x);
    }

    @Override
    public void setY(int _y){
        rectangle.translate(0, _y-y);
        super.setY(_y);
    }
    
}
