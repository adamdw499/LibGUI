package com.mason.libgui.components.misc;

import com.mason.libgui.core.UIComponentManager;

/**
 * For when the UIComponent needs access to its parent component manager, but cannot access this information upon
 * instantiation.
 */
public interface Fosterable{

    /**
     * Grants this object its parent component manager, upon being added to it.
     * @param parent
     */
    void setParent(UIComponentManager parent);

}
