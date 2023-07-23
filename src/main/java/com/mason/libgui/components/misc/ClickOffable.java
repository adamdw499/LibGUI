package com.mason.libgui.components.misc;

/**
 * An interface for UIComponents that need to be notified when they are clicked away from.
 */
public interface ClickOffable{

    /**
     * Notifies the UIComponent that it has been clicked away from.
     */
    void clickOff();

}
