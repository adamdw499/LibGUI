package com.mason.libgui.testHelpers.behaviours;

import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Boundable;
import com.mason.libgui.utils.structures.Coord;

public class MouseInputEventPrinter implements BoundedMouseInputListener{


    private final Boundable boundable;


    public MouseInputEventPrinter(Boundable boundable){
        this.boundable = boundable;
    }


    @Override
    public boolean withinBounds(Coord c){
        return boundable.withinBounds(c);
    }


    @Override
    public void onMouseMoved(MouseInputEvent e){
        System.out.println("Mouse moved: " + boundable.toString());
    }
    @Override
    public void onMouseDragged(MouseInputEvent e){
        System.out.println("Mouse dragged: " + boundable.toString());
    }

    @Override
    public void onMousePressed(MouseInputEvent e){
        System.out.println("Mouse pressed: " + boundable.toString());
    }
    @Override
    public void onMouseReleased(MouseInputEvent e){
        System.out.println("Mouse released: " + boundable.toString());
    }

    @Override
    public void onMouseClicked(MouseInputEvent e){
        System.out.println("Mouse clicked: " + boundable.toString());
    }

    @Override
    public void onMouseWheel(MouseInputEvent e){
        System.out.println("Mouse wheeled: " + boundable.toString());
    }

}
