package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.mouse.MouseInputEvent;

import java.util.function.Function;

public class GUIMouseInputTransformer extends SimpleGUIInputGate{


    private Function<MouseInputEvent, MouseInputEvent> transform;


    public GUIMouseInputTransformer(){

    }


    public final void setTransform(Function<MouseInputEvent, MouseInputEvent> transform){
        this.transform = transform;
    }


    @Override
    public void onMouseMoved(MouseInputEvent e){
        e = transform.apply(e);
        super.onMouseMoved(e);
    }

    @Override
    public void onMouseDragged(MouseInputEvent e){
        e = transform.apply(e);
        super.onMouseDragged(e);
    }

    @Override
    public void onMousePressed(MouseInputEvent e){
        e = transform.apply(e);
        super.onMousePressed(e);
    }

    @Override
    public void onMouseReleased(MouseInputEvent e){
        e = transform.apply(e);
        super.onMouseReleased(e);
    }

    @Override
    public void onMouseClicked(MouseInputEvent e){
        e = transform.apply(e);
        super.onMouseClicked(e);
    }

    @Override
    public void onMouseWheel(MouseInputEvent e){
        e = transform.apply(e);
        super.onMouseWheel(e);
    }

}
