package com.mason.libgui.core.input.mouse;

public interface MouseInputListener{

    default void onMouseMoved(MouseInputEvent e){}
    default void onMouseDragged(MouseInputEvent e){}

    default void onMousePressed(MouseInputEvent e){}
    default void onMouseReleased(MouseInputEvent e){}

    default void onMouseClicked(MouseInputEvent e){}

    default void onMouseWheel(MouseInputEvent e){}

}
