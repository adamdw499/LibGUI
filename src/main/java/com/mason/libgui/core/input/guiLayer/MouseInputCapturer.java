package com.mason.libgui.core.input.guiLayer;

import com.mason.libgui.core.input.componentLayer.GUIInputRegister;
import com.mason.libgui.core.input.mouse.BoundedMouseInputListener;
import com.mason.libgui.core.input.mouse.MouseInputEvent;
import com.mason.libgui.utils.structures.Coord;

public class MouseInputCapturer extends SimpleGUIInputGate implements BoundedMouseInputListener{


    @Override
    public boolean withinBounds(Coord c){
        return true;
    }

}
