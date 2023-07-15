package com.mason.libgui.components.inventory;

import com.mason.libgui.components.buttons.Button;
import com.mason.libgui.components.buttons.ButtonImpersonator;
import com.mason.libgui.components.panes.Pane;
import com.mason.libgui.core.UIComponent;
import com.mason.libgui.utils.StyleInfo;
import com.mason.libgui.utils.UIAligner.Direction;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import static com.mason.libgui.components.buttons.TrapezoidalButton.getBlankButton;
import static com.mason.libgui.utils.UIAligner.Direction.*;
import static java.lang.Math.min;

public class Inventory extends UIComponent{


    private Pane[] panes;
    private PaneSelector selector;
    private int currentPane = 0;
    private Direction direction;


    public Inventory(StyleInfo info, Pane[] panes, Button[] selectorButtons, Direction direction){
        super(panes[0].getX(), panes[0].getY(), panes[0].getWidth(), panes[0].getHeight());
        this.panes = panes;
        if(direction.vertical){
            selector = new PaneSelector(info, 0, 0, width, selectorButtons[0].getHeight(), selectorButtons, direction);
            setHeight(panes[0].getHeight() + selector.getHeight());
        }else{
            selector = new PaneSelector(info, 0, 0, selectorButtons[0].getWidth(), height, selectorButtons, direction);
            setWidth(panes[0].getWidth() + selector.getWidth());
        }
        this.direction = direction;
        setX(x);
        setY(y);
    }

    public Inventory(StyleInfo info, Pane[] panes, Direction direction){
        this(info, panes, defaultSelectorButtons(info, panes[0], panes.length, direction), direction);
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

        if(direction.equals(LEFT)){
            for(Pane pane : panes) pane.setX(_x + selector.getWidth());
        }else{
            for(Pane pane : panes) pane.setX(_x);
        }

        if(direction.equals(RIGHT)){
            selector.setX(_x + panes[currentPane].getWidth());
        }else{
            selector.setX(_x);
        }
    }

    public void setY(int _y){
        super.setY(_y);

        if(direction.equals(UP)){
            for(Pane pane : panes) pane.setY(_y + selector.getHeight());
        }else{
            for(Pane pane : panes) pane.setY(_y);
        }

        if(direction.equals(DOWN)){
            selector.setY(_y + panes[currentPane].getHeight());
        }else{
            selector.setY(_y);
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


    private class PaneSelectorButton extends ButtonImpersonator {


        private final int paneNum;


        public PaneSelectorButton(Button b, int paneNum){
            super(b);
            this.paneNum = paneNum;
        }


        @Override
        public void mouseClicked(MouseEvent e){
            Inventory.this.setPane(paneNum);
        }

    }


    private static Button[] defaultSelectorButtons(StyleInfo info, Pane pane, int numPanes, Direction direction){
        Button[] selectors = new Button[numPanes];

        int x = pane.getX(), y = pane.getY(), w, h, tapering = min(24, 24*3/numPanes);
        if(direction.vertical){
            h = 24;
            w = pane.getWidth()/numPanes;
        }else{
            w = 24;
            h = pane.getHeight()/numPanes;
        }

        for(int n=0; n<numPanes; n++){
            selectors[n] = getBlankButton(info, x, y, w, h, tapering, direction);
            if(direction.vertical){
                x += w;
            }else{
                y += h;
            }
        }

        return selectors;
    }

}
