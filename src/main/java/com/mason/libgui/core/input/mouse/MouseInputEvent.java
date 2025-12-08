package com.mason.libgui.core.input.mouse;

import com.mason.libgui.utils.structures.Coord;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseInputEvent{


    private final MouseEvent event;
    private final MouseInputType type;
    private Coord coord;


    public MouseInputEvent(MouseEvent event){
        this.event = event;
        coord = new Coord(event.getX(), event.getY());
        type = MouseInputType.fromRawMouseEvent(event);
    }


    public Coord getCoord(){
        return coord;
    }

    public void setCoord(Coord c){
        coord = c;
    }

    public MouseInputEvent copy(){
        MouseInputEvent copy = new MouseInputEvent(event);
        copy.setCoord(coord);
        return copy;
    }


    public MouseInputType getType(){
        return type;
    }

    public void setCoordRelativeTo(Coord topLeft){
        coord = new Coord(coord.x() - topLeft.x(), coord.y() - topLeft.y());
    }


    public int getWheelMotion(){
        validateTypeIsMouseWheel();
        return ((MouseWheelEvent) event).getWheelRotation();
    }

    private void validateTypeIsMouseWheel(){
        if(!type.equals(MouseInputType.WHEEL)){
            throw new IllegalStateException("Attempting to get wheel state of non-wheel event");
        }
    }

}
