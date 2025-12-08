package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.core.input.componentLayer.UIComponentManagerInputDistributor;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Coord;

public class ViewportInputDistributor extends UIComponentManagerInputDistributor{


    private final PanZoomBehaviour behaviour;
    private final ViewportMouseInputCapturer viewportCapturer;
    private boolean inputConsumed = false;


    public ViewportInputDistributor(PanZoomBehaviour behaviour, ViewportMouseInputCapturer viewportCapturer){
        this.behaviour = behaviour;
        this.viewportCapturer = viewportCapturer;
    }


    @Override
    public void onMouseInput(MouseInputEvent event){
        if(viewportCapturer.isActive()){
            dispatchMouseInputEventToListener(event, behaviour);
        }else{
            onMouseInputWithoutCapture(event);
        }
    }

    private void onMouseInputWithoutCapture(MouseInputEvent event){
        passScreenInputToMouseInputListeners(event);
        if(!inputConsumed){
            dispatchMouseInputEventToListener(event, behaviour);
        }
    }

    private void passScreenInputToMouseInputListeners(MouseInputEvent event){
        MouseInputEvent apparentEvent = getApparentMouseInputEvent(event);
        passApparentInputToMouseInputListeners(apparentEvent);
    }

    private MouseInputEvent getApparentMouseInputEvent(MouseInputEvent event){
        Coord apparentCoord = behaviour.screenToApparent(event.getCoord());
        MouseInputEvent copy = event.copy();
        copy.setCoord(apparentCoord);
        return copy;
    }

    private void passApparentInputToMouseInputListeners(MouseInputEvent event){
        inputConsumed = false;
        for(BoundedMouseInputListener listener : mouseListeners()){
            if(listener.withinBounds(event.getCoord())){
                dispatchMouseInputEventToListener(event, listener);
                inputConsumed = true;
                return;
            }
        }
    }

}
