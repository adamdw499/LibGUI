
package com.mason.libgui.core;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The base object that can be rendered, intersected with, and animated on screen.
 * @author Adam Whittaker
 */
public abstract class UIComponent extends MouseAdapter{
    
    
    protected int x, y, width, height;
    
    
    public UIComponent(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        width = w;
        height = h;
    }
    
    
    /**
     * Checks whether the given coordinate is inside the given rectangle.
     * @param x The x coordinate of the top-left of the rectangle.
     * @param y The y coordinate of the top-left of the rectangle.
     * @param width The width.
     * @param height The height.
     * @param mx The x coordinate.
     * @param my The y coordinate.
     * @return True if it is.
     */
    public static boolean withinBounds(int x, int y, int width, int height, int mx, int my){
        return x<mx && mx<x+width && y<my && my<y+height;
    }
    
    /**
     * Checks whether the given coordinate is inside this component.
     * @param mx The x coordinate.
     * @param my The y coordinate.
     * @return True if it is.
     */
    public boolean withinBounds(int mx, int my){
        return withinBounds(x, y, width, height, mx, my);
    }
    
    
    /**
     * Retrieves the box's x value.
     */
    public int getX(){
        return x;
    }
    
    /**
     * Retrieves the box's y value.
     */
    public int getY(){
        return y;
    }
    
    /**
     * Retrieves the box's width.
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Retrieves the box's height.
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * Sets the box's position.
     * @param _x The x
     */
    public void setX(int _x){
        x = _x;
    }
    
    /**
     * Sets the box's position.
     * @param _y The y
     */
    public void setY(int _y){
        y = _y;
    }
    
    /**
     * Changes the box's dimensions.
     * @param w The width.
     */
    public void setWidth(int w){
        width = w;
    }
    
    /**
     * Changes the box's dimensions.
     * @param h The height.
     */
    public void setHeight(int h){
        height = h;
    }

    /**
     * Checks if this component intersects with the given one.
     * @param comp The component
     * @return true if it does
     */
    public boolean intersects(UIComponent comp){
        return comp.x + comp.width >= x && comp.x <= x + width &&
                comp.y + comp.height >= y && comp.y <= y + height;
    }


    /**
     * Renders the component
     * @param g the graphics object
     */
    public abstract void render(Graphics2D g);

    /**
     * Ticks this component.
     * @param mx mouse x
     * @param my mouse y
     */
    public abstract void tick(int mx, int my);


    /**
     * Grants this object its parent component manager, upon being added to it.
     */
    public void setParent(UIComponentManager parent){}


    public static UIComponent getTestInstance(int x, int y, int w, int h){
        return new UIComponent(x, y, w, h){

            @Override
            public void render(Graphics2D g){
                g.setColor(Color.PINK);
                g.fillRect(x, y, width, height);
            }

            @Override
            public void tick(int mx, int my){}

            @Override
            public void mousePressed(MouseEvent e){
                System.out.println("Test component pressed");
            }

        };
    }
    
}
