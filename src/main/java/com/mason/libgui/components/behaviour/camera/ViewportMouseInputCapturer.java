package com.mason.libgui.components.behaviour.camera;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.guiLayer.MouseInputCapturer;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;

public class ViewportMouseInputCapturer extends MouseInputCapturer{


    private boolean active = false;


    public ViewportMouseInputCapturer(){

    }


    boolean isActive(){
        return active;
    }


    @Override
    public void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        validateInputSourceType(inputSource);
        active = true;
    }

    @Override
    public void unsetInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        validateInputSourceType(inputSource);
        active = false;
    }

    private void validateInputSourceType(GUIInputRegister<BoundedMouseInputListener> inputSource){
        if(!(inputSource instanceof ViewportInputDistributor)){
            throw new IllegalArgumentException("Using invalid register type with ViewportMouseInputCapturer");
        }
    }

}
