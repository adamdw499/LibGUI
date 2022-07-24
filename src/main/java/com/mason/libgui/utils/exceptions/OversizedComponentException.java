package com.mason.libgui.utils.exceptions;

import com.mason.libgui.core.UIComponent;

public class OversizedComponentException extends IllegalStateException{


    public OversizedComponentException(UIComponent component, UIComponent superComponent){
        super("Component \"" + component.toString() + "\" does not fit inside its super component \"" +
                superComponent.toString() + "\".\n "
                + String.format("Component dimensions: [x: %d, y : %d, width: %d, height: %d]",
                component.getX(), component.getY(), component.getWidth(), component.getHeight())
                + "\n" + String.format("Super dimensions: [x: %d, y : %d, width: %d, height: %d]",
                superComponent.getX(), superComponent.getY(), superComponent.getWidth(),
                superComponent.getHeight()));
    }

}
