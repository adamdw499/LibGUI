package com.mason.libgui.components.buttons;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Copies a Button, overriding its mouse click function. Intended for use with blank Buttons.
 */
public abstract class ButtonImpersonator extends Button{


    /**
     * The button that is being impersonated.
     */
    private final Button button;


    public ButtonImpersonator(Button b){
        super(b.info, b.getX(), b.getY(), b.getWidth(), b.getHeight());
        button = b;
    }


    @Override
    public void render(Graphics2D g){
        button.render(g);
    }

    @Override
    public abstract void mouseClicked(MouseEvent e);


    @Override
    public boolean withinBounds(int mx, int my){
        return button.withinBounds(mx, my);
    }

    @Override
    public void tick(int mx, int my){
        button.tick(mx, my);
    }

    @Override
    public void setX(int x){
        super.setX(x);
        button.setX(x);
    }

    @Override
    public void setY(int y){
        super.setY(y);
        button.setY(y);
    }

    @Override
    protected boolean isHovering(){
        return button.isHovering();
    }

    @Override
    protected void startHovering(){
        button.startHovering();
    }

    @Override
    protected void stopHovering(){
        button.stopHovering();
    }

    @Override
    public void mouseMoved(MouseEvent e){
        button.mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        button.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        button.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        button.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        button.mouseExited(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        button.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e){
        button.mouseDragged(e);
    }

}
