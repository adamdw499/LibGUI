package com.mason.libgui.core.input.mouse;

import java.awt.event.MouseEvent;

public enum MouseInputType{

    ENTER,
    EXIT,

    MOVE,
    DRAG,

    PRESS,
    RELEASE,

    CLICK,

    WHEEL;


    public static MouseInputType fromRawMouseEvent(MouseEvent event){
        return switch(event.getID()){
            case MouseEvent.MOUSE_ENTERED -> ENTER;
            case MouseEvent.MOUSE_EXITED -> EXIT;
            case MouseEvent.MOUSE_MOVED -> MOVE;
            case MouseEvent.MOUSE_DRAGGED -> DRAG;
            case MouseEvent.MOUSE_PRESSED -> PRESS;
            case MouseEvent.MOUSE_RELEASED -> RELEASE;
            case MouseEvent.MOUSE_CLICKED -> CLICK;
            case MouseEvent.MOUSE_WHEEL -> WHEEL;
            default -> throw new IllegalStateException("Unexpected MouseEvent id: " + event.getID());
        };
    }

}
