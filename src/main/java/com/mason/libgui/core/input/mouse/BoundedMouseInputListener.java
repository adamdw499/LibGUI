package com.mason.libgui.core.input.mouse;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.utils.structures.Boundable;

public interface BoundedMouseInputListener extends MouseInputListener, Boundable, InputDelegator{


    @Override
    default void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        inputSource.addMouseInputListener(this);
    }

    @Override
    default void unsetInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource){
        inputSource.removeMouseInputListener(this);
    }
}
