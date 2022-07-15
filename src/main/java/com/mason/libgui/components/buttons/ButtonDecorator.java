package com.mason.libgui.components.buttons;

import com.mason.libgui.components.inventory.Inventory;
import com.mason.libgui.utils.StyleInfo;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class ButtonDecorator extends Button{


    private Button button;


    public ButtonDecorator(Button b){
        super(b.style, b.getX(), b.getY(), b.getWidth(), b.getHeight());
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

    public void setX(int x){
        super.setX(x);
        button.setX(x);
    }

    public void setY(int y){
        super.setY(y);
        button.setY(y);
    }

    protected boolean isHovering(){
        return button.isHovering();
    }

    @Override
    public void mouseMoved(MouseEvent e){
        button.mouseMoved(e);
    }

    public void mousePressed(MouseEvent e) {
        button.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        button.mouseReleased(e);
    }

    public void mouseEntered(MouseEvent e) {
        button.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        button.mouseExited(e);
    }

    public void mouseWheelMoved(MouseWheelEvent e){
        button.mouseWheelMoved(e);
    }

    public void mouseDragged(MouseEvent e){
        button.mouseDragged(e);
    }

}
