package com.mason.libgui.core.input.mouse;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;

public interface InputDelegator{

    void setInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource);

    void unsetInputSource(GUIInputRegister<BoundedMouseInputListener> inputSource);

}
