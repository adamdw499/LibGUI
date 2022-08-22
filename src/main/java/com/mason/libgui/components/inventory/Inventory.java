package com.mason.libgui.components.inventory;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.buttons.ButtonDecorator;
import com.mason.libgui.components.panes.Pane;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class Inventory extends UIComponent{


    private Pane[] panes;
    private PaneSelector selector;
    private int currentPane = 0;
    private Direction direction;


    public Inventory(StyleInfo info, Pane[] panes, Button[] buttons, Direction direction){
        super(panes[0].getX(), panes[0].getY(), panes[0].getWidth(), panes[0].getHeight());
        this.panes = panes;
        switch(direction) {
            case DOWN, UP -> selector = new PaneSelector(info, 0, 0, width, buttons[0].getHeight(), buttons, direction);
            case LEFT, RIGHT -> selector = new PaneSelector(info, 0, 0, buttons[0].getWidth(), height, buttons, direction);
        }
        this.direction = direction;
        setX(x);
        setY(y);
        switch(direction){
            case UP, DOWN -> setHeight(panes[0].getHeight() + selector.getHeight());
            case LEFT, RIGHT -> setWidth(panes[0].getWidth() + selector.getWidth());
        }
    }


    @Override
    public void render(Graphics2D g){
        panes[currentPane].render(g);
        selector.render(g);
    }

    protected void setPane(int paneNum){
        currentPane = paneNum;
    }

    @Override
    public boolean withinBounds(int mx, int my){
        return panes[currentPane].withinBounds(mx, my) || selector.withinBounds(mx, my);
    }

    @Override
    public void tick(int mx, int my){
        for(Pane pane : panes) pane.tick(mx, my);
        selector.tick(mx, my);
    }


    public void setX(int _x){
        super.setX(_x);
        for(Pane pane : panes) pane.setX(_x);
        switch(direction){
            case DOWN, UP -> selector.setX(_x);
            case LEFT -> selector.setX(_x-selector.getWidth());
            case RIGHT -> selector.setX(_x+panes[currentPane].getWidth());
        }
    }

    public void setY(int _y){
        super.setY(_y);
        for(Pane pane : panes) pane.setY(_y);
        switch(direction){
            case LEFT, RIGHT -> selector.setY(_y);
            case DOWN -> selector.setY(_y+panes[currentPane].getHeight());
            case UP -> selector.setY(_y-selector.getHeight());
        }
    }


    @Override
    public void mouseClicked(MouseEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mouseClicked(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mousePressed(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mouseReleased(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mouseReleased(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mouseWheelMoved(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mouseDragged(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e){
        if(panes[currentPane].withinBounds(e.getX(), e.getY()))  panes[currentPane].mouseMoved(e);
        else if(selector.withinBounds(e.getX(), e.getY())) selector.mouseMoved(e);
    }


    private class PaneSelector extends Pane{


        public PaneSelector(StyleInfo info, int x, int y, int w, int h, Button[] buttons, Direction direction){
            super(info, x, y, w, h);
            for(int n=0; n<buttons.length; n++){
                setButtonCoords(w, h, buttons[n], direction, n, buttons.length);
                addComponent(new PaneSelectorButton(buttons[n], n));
            }
        }


        private static void setButtonCoords(int w, int h, Button button, Direction direction, int n, int numButtons){
            switch(direction){
                case DOWN, UP -> {
                    button.setWidth(w/numButtons);
                    button.setX(button.getWidth()*n);
                    button.setY(0);
                }
                case LEFT, RIGHT -> {
                    button.setHeight(h/numButtons);
                    button.setX(0);
                    button.setY(button.getHeight()*n);
                }
            }
        }


        @Override
        public void render(Graphics2D g){
            renderComponents(g);
        }

    }


    private class PaneSelectorButton extends ButtonDecorator{


        private int paneNum;


        public PaneSelectorButton(Button b, int paneNum){
            super(b);
            this.paneNum = paneNum;
        }


        @Override
        public void mouseClicked(MouseEvent e){
            Inventory.this.setPane(paneNum);
        }

    }

}
