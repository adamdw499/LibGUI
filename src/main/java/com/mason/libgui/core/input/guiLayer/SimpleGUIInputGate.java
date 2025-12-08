package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;

import java.awt.event.KeyEvent;

public class SimpleGUIInputGate implements GUIInputGate{


    private GUIInputSocket delegate;


    public SimpleGUIInputGate(){}


    @Override
    public void setDelegateSocket(GUIInputSocket delegate){
        this.delegate = delegate;
    }


    @Override
    public void keyTyped(KeyEvent e){
        delegate.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e){
        delegate.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e){
        delegate.keyReleased(e);
    }

    @Override
    public void onMouseMoved(MouseInputEvent e){
        delegate.onMouseMoved(e);
    }

    @Override
    public void onMouseDragged(MouseInputEvent e){
        delegate.onMouseDragged(e);
    }

    @Override
    public void onMousePressed(MouseInputEvent e){
        delegate.onMousePressed(e);
    }

    @Override
    public void onMouseReleased(MouseInputEvent e){
        delegate.onMouseReleased(e);
    }

    @Override
    public void onMouseClicked(MouseInputEvent e){
        delegate.onMouseClicked(e);
    }

    @Override
    public void onMouseWheel(MouseInputEvent e){
        delegate.onMouseWheel(e);
    }

}
