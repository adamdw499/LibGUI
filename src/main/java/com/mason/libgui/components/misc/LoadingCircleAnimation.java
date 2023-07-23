
package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponent;
import com.mason.libgui.core.Window;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A simple rotating circle animation.
 * @author Adam Whittaker
 */
public class LoadingCircleAnimation extends UIComponent{
    
    
    private final Color color;    
    private final double angularVelocity;    
    private final AffineTransform rotation = new AffineTransform();
    private final Rectangle rectangle;
    private final int lineWidth;


    /**
     * Creates an instance.
     * @param col The color of the circle
     * @param x The top left x
     * @param y top left y
     * @param diam diameter of the circle
     * @param angularVel angular velocity of the animation
     * @param gapHeight the size of the gap in the circle that does the rotating
     * @param lineWidth the width of the loading circle annulus
     */
    public LoadingCircleAnimation(Color col, int x, int y, int diam, double angularVel, int gapHeight, int lineWidth){
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
        g.fillOval(x + lineWidth, y + lineWidth, width - 2*lineWidth, height - 2*lineWidth);
        g.fill(rotation.createTransformedShape(rectangle));
    }

    /**
     * Rotates the gap in the annulus a little.
     * @param mx
     * @param my
     */
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
