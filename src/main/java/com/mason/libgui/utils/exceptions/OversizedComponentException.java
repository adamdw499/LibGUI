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

    public OversizedComponentException(UIComponent superComponent){
        super("Component \"" + superComponent.toString() + "\" is too small to support the given subcomponents.\n "
                + String.format("Dimensions: [x: %d, y : %d, width: %d, height: %d]",
                superComponent.getX(), superComponent.getY(), superComponent.getWidth(),
                superComponent.getHeight()));
    }

    public OversizedComponentException(int width, int height){
        super("The given packing problem is too small for the box [width: " + width + ", height: " + height + "].");
    }

    public OversizedComponentException(UIComponent component, int width, int height){
        super("Component \"" + component.toString() + "\" does not fit inside the given box.\n"
                + String.format("Component dimensions: [x: %d, y : %d, width: %d, height: %d]",
                component.getX(), component.getY(), component.getWidth(), component.getHeight())
                + "\n" + String.format("box dimensions: [width: %d, height: %d]", width, height));
    }

    public OversizedComponentException(){}

}
