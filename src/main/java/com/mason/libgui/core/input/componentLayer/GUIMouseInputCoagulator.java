package com.mason.libgui.core.input.componentLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.core.input.mouse.MouseInputListener;

public interface GUIMouseInputCoagulator extends MouseInputListener{


    @Override
    public default void onMouseMoved(MouseInputEvent e){
        onMouseInput(e);
    }

    @Override
    public default void onMouseDragged(MouseInputEvent e){
        onMouseInput(e);
    }

    @Override
    public default void onMousePressed(MouseInputEvent e){
        onMouseInput(e);
    }

    @Override
    public default void onMouseReleased(MouseInputEvent e){
        onMouseInput(e);
    }

    @Override
    public default void onMouseClicked(MouseInputEvent e){
        onMouseInput(e);
    }

    @Override
    public default void onMouseWheel(MouseInputEvent e){
        onMouseInput(e);
    }


    public abstract void onMouseInput(MouseInputEvent e);

}
