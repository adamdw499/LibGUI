package com.mason.libgui.core.input.componentLayer;

import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.core.input.mouse.MouseInputListener;

import java.awt.event.*;

public class UIComponentManagerInputDistributor implements GUIMouseInputCoagulator, GUIInputDistributor<BoundedMouseInputListener>{


    private final ListenerRegister<KeyListener> keyRegister;
    private final ListenerRegister<BoundedMouseInputListener> mouseRegister;


    public UIComponentManagerInputDistributor(){
        keyRegister = new OrderedListenerRegister<>();
        mouseRegister = new OrderedListenerRegister<>();
    }


    protected Iterable<BoundedMouseInputListener> mouseListeners(){
        return mouseRegister;
    }


    @Override
    public void addKeyListener(KeyListener listener){
        keyRegister.addListener(listener);
    }

    @Override
    public void removeKeyListener(KeyListener listener){
        keyRegister.removeListener(listener);
    }

    @Override
    public void addMouseInputListener(BoundedMouseInputListener listener){
        mouseRegister.addListener(listener);
    }

    @Override
    public void removeMouseInputListener(BoundedMouseInputListener listener){
        mouseRegister.removeListener(listener);
    }


    @Override
    public void onMouseInput(MouseInputEvent event){
        for(BoundedMouseInputListener listener : mouseRegister){
            if(listener.withinBounds(event.getCoord())){
                dispatchMouseInputEventToListener(event, listener);
                break;
            }
        }
    }

    protected final void dispatchMouseInputEventToListener(MouseInputEvent event, MouseInputListener listener){
        switch(event.getType()){

            case MOVE -> listener.onMouseMoved(event);
            case DRAG -> listener.onMouseDragged(event);

            case PRESS -> listener.onMousePressed(event);
            case RELEASE -> listener.onMouseReleased(event);

            case CLICK -> listener.onMouseClicked(event);
            case WHEEL -> listener.onMouseWheel(event);
        }
    }

    @Override
    public void keyTyped(KeyEvent e){
        keyRegister.forEach(listener -> listener.keyTyped(e));
    }

    @Override
    public void keyPressed(KeyEvent e){
        keyRegister.forEach(listener -> listener.keyPressed(e));
    }

    @Override
    public void keyReleased(KeyEvent e){
        keyRegister.forEach(listener -> listener.keyReleased(e));
    }

}
