package com.mason.libgui.components.behaviour.drag;

import com.mason.libgui.components.behaviour.MouseCaptureBehaviour;
import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.guiLayer.GUIInputSocket;
import com.mason.libgui.core.input.guiLayer.MouseInputCapturer;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;

public abstract class AbstractDragBehaviour implements BoundedMouseInputListener, GUIInputSocket{

    private final MouseCaptureBehaviour mouseCapture;
    private boolean currentlyDragging = false;


    protected AbstractDragBehaviour(){
        mouseCapture = MouseCaptureBehaviour.buildWithDelegateSocket(this);
    }

    protected AbstractDragBehaviour(MouseInputCapturer mouseInputCapturer){
        mouseCapture = MouseCaptureBehaviour.buildWithDelegateSocketAndCustomCapturer(this, mouseInputCapturer);
    }


    public boolean isCurrentlyDragging(){
        return currentlyDragging;
    }

    protected MouseCaptureBehaviour getMouseCaptureBehaviour(){
        return mouseCapture;
    }


    @Override
    public void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        mouseCapture.setInputRegister(inputSource);
        BoundedMouseInputListener.super.setInputSource(inputSource);
    }


    @Override
    public void onMousePressed(MouseInputEvent event){
        if(!currentlyDragging){
            currentlyDragging = true;
            mouseCapture.captureMouse();
            onDragStart(event);
        }
    }

    protected abstract void onDragStart(MouseInputEvent event);


    @Override
    public void onMouseDragged(MouseInputEvent event){
        if(currentlyDragging){
            onDragIncrement(event);
        }
    }

    protected abstract void onDragIncrement(MouseInputEvent event);


    @Override
    public void onMouseReleased(MouseInputEvent event){
        if(currentlyDragging){
            currentlyDragging = false;
            mouseCapture.releaseMouse();
            onDragRelease(event);
        }
    }

    protected abstract void onDragRelease(MouseInputEvent event);

}
