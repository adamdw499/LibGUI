
package com.mason.libgui.core;

import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;

/**
 *
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
        return x<mx && mx<x+width && y<my && my<y+height;
    }
    
    
    /**
     * Retrieves the box's x value.
     * @return
     */
    public int getX(){
        return x;
    }
    
    /**
     * Retrieves the box's y value.
     * @return
     */
    public int getY(){
        return y;
    }
    
    /**
     * Retrieves the box's width.
     * @return
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Retrieves the box's height.
     * @return
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
    protected void setWidth(int w){
        width = w;
    }
    
    /**
     * Changes the box's dimensions.
     * @param h The height.
     */
    protected void setHeight(int h){
        height = h;
    }
    
    
    public void render(Graphics2D g){}
    
    public void tick(int mx, int my){}
    
}
